'use strict';

const functions = require('firebase-functions');
const nodemailer = require('nodemailer');
const storage = require('@google-cloud/storage')();
const admin = require('firebase-admin');

// Configure the email transport using the default SMTP transport and a GMail account.
// For Gmail, enable these:
// 1. https://www.google.com/settings/security/lesssecureapps
// 2. https://accounts.google.com/DisplayUnlockCaptcha
// For other types of transports such as Sendgrid see https://nodemailer.com/transports/
// TODO: Configure the `gmail.email` and `gmail.password` Google Cloud environment variables.
const gmailEmail = functions.config().gmail.email;
const gmailPassword = functions.config().gmail.password;
const mailTransport = nodemailer.createTransport({
  service: 'gmail',
  auth: {
    user: gmailEmail,
    pass: gmailPassword
  }
});

// Your company name to include in the emails
// TODO: Change this to your app or company name to customize the email sent.
const APP_NAME = `TutoGuia Firebase`;

// [START sendWelcomeEmail]
/**
 * Sends a welcome email to new user.
 */
exports.sendWelcomeEmail = functions.auth.user().onCreate(event => {
	// [START eventAttributes]
	const user = event.data; // The Firebase user.

	const email = user.email; // The email of the user.
	const displayName = user.displayName; // The display name of the user.
	// [END eventAttributes]

	return sendWelcomeEmail(email, displayName);
});

// Sends a welcome email to the given user.
function sendWelcomeEmail(email, displayName) {
	const mailOptions = {
		from: `${APP_NAME} <noreply@firebase.com>`,
    		to: email
  	};

  	// The user subscribed to the newsletter.
  	mailOptions.subject = `Welcome to ${APP_NAME}!`;
  	mailOptions.text = `Hey ${displayName || ''}! Welcome to ${APP_NAME}. I hope you will enjoy our service.`;
  	return mailTransport.sendMail(mailOptions).then(() => {
    		console.log('New welcome email sent to:', email);
  	});
}
// [END sendWelcomeEmail]

// [START parseTextFile]

admin.initializeApp(functions.config().firebase);

/**
 * Parse a text file uploaded to storage.
 */
exports.parseTextFile = functions.storage.object().onChange(event => {
	const file = event.data;
        if (!file){
            console.log("not a file event");
            return;
        }
        if (file.resourceState === 'not_exists'){
            console.log("file deletion event");
            return;
        }
        if (!file.contentType.startsWith('text/')){
            console.log("not a text file: " + file.contentType);
            return;
        }
        if (!file.bucket){
            console.log("bucket not provided");
            return;
        }
        if (!file.name){
            console.log("file name not provided");
            return ;
        }
        (storage
         .bucket(file.bucket)
         .file(file.name)
         .download()
         .then(function(data){
             	if (data)
                 	return data.toString('utf-8');
         	})
         .then(function(data){
             	if (data) {
                 	console.log("new file "+file.name);
                 	return parseDataText(data);
             	}
         	})
	);
});

function parseDataText(data){
	const dataSplit = data.split('\n');
	return admin.database().ref("File").set(dataSplit);
}

// [END parseTextFile]
