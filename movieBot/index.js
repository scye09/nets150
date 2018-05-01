'use strict';
const BootBot = require('bootbot');
const config = require('config');
var java = require("java");
java.classpath.push("commons-lang3-3.1.jar");
java.classpath.push("commons-io.jar");
java.classpath.push("Main.jar");
java.classpath.push("Movie.jar");
java.classpath.push("Corpus.jar");
java.classpath.push("VectorSpaceModel.jar")
java.classpath.push("Main$ValueComparator.jar");

const bot = new BootBot({
  accessToken: config.get('accessToken'),
  verifyToken: config.get('verifyToken'),
  appSecret: config.get('appSecret')
});

// var fs = require('fs');
// fs.readFile('./test.java', (err, data) => {
//   if (err) throw err;
//   console.log(data);
// });

bot.setGreetingText("Hello!");

 bot.on('message', (payload, chat) => {
   const text = payload.message.text;
   chat.say(`Echo: ${text}`);
 });

bot.hear(['hi', 'hello', 'hey'], (payload, chat) => {
  chat.say('Type "1" to get movie recommendations based on other movie titles; type "2" to get movie recommendations'
          + 'based on movie descriptions.');
});

bot.hear('1', (payload, chat) => {
  chat.say('Type "title" and enter a movie title:');
});

bot.hear(/title (.*)/i, (payload, chat, data) => {
  const query =  String(data.match[1]);
  const res = query.toLowerCase();
  chat.say("I hear you.");
  // var test = new Packages.test();
  // System.out.println(test.tests("Interstellar"));
  // chat.say(test.tests("Interstellar"));
});

java.callStaticMethod("Main", "getMovieGivenTitle", "The Hunger Games", function(err, results) {
  if (err) {
    console.error(err); return;
  }
  // do something with results
  console.log(results);

});

bot.hear('2', (payload, chat) => {
  chat.say('Enter keywords from movie descriptions:');
});

bot.start(config.get('botPort'));
