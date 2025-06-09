const functions = require("firebase-functions/v1");
const admin = require("firebase-admin");
admin.initializeApp();

exports.sendNotificationOnMessage = functions.database
  .ref("/message/{channelId}/{messageId}")
  .onCreate(async (snapshot, context) => {
    const messageData = snapshot.val();
    const channelId = context.params.channelId;

    if (!messageData || !messageData.message || !messageData.senderName) {
      return null;
    }

    const payload = {
      notification: {
        title: `New message in ${channelId}`,
        body: `${messageData.senderName}: ${messageData.message}`,
      },
      topic: `group_${channelId}`,
    };

    try {
      await admin.messaging().send(payload);
      console.log("Notification sent:", payload);
    } catch (error) {
      console.error("Error sending notification:", error);
    }

    return null;
  });
