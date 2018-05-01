'use strict';
const BootBot = require('bootbot');
const config = require('config');

const bot = new BootBot({
  accessToken: config.get('accessToken'),
  verifyToken: config.get('verifyToken'),
  appSecret: config.get('appSecret')
});

/**
 * Demo handler to echo back whatever the user says.
 * Feel free to delete this handler and start hacking!
 */
 // bot.setGetStartedButton((payload, chat) => {
 //   const welcome1 = `Hey there, trainer! How well you think you ...`;
 //   const welcome2 = `Type START or PLAY to join the challenge!`;
 //   const options = { typing: true };
 //   chat.say(welcome1, options)
 //     .then(() => chat.say(welcome2, options));
 // });


 bot.on('message', (payload, chat) => {
   console.log("sdfs");
   const text = payload.message.text;
   chat.say(`Echo: ${text}`);
 });

bot.hear(['hi', 'hello', 'hey'], (payload, chat) => {
  chat.say('Hello, Type Genre then add the movie genre of the recommendation you want. ');
  console.log("I'M WORKING!!");
});

bot.start(config.get('botPort'));
