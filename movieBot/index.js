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

// Listen for keywords to start the bot
bot.hear(['hi', 'hello', 'hey'], (payload, chat) => {
  chat.say('Type "1" to get movie recommendations based on other movie titles; type "2" to get movie recommendations'
          + 'based on movie descriptions.');
});

// When 1 is typed, prompt user to enter "title someMovieTitle"
bot.hear('1', (payload, chat) => {
  chat.say('Type "title" and enter a movie title:');
  
});

// Listen for the title the user entered and get best match
bot.hear(/title (.*)/i, (payload, chat, data) => {
  const query =  String(data.match[1]);
  const movie = query.toLowerCase();
  // chat.say("I hear you.");
  // var test = new Packages.test();
  // System.out.println(test.tests("Interstellar"));
  // chat.say(test.tests("Interstellar"));

  // const movie = payload.message.text;
  // console.log(movie);

  java.callStaticMethod("Main", "getMovieGivenTitle", movie, function(err, results) {
    if (err) {
      console.error(err); return;
    }
    // do something with results
    // console.log(results);
    chat.say("We recommend you watch " +  results);
  
  });

});


//When 2 is typed, prompt user to enter "summary someKeywords"
bot.hear('2', (payload, chat) => {
  chat.say('Type "summary" and then enter keywords from movie descriptions:');
  
});

// Listen for the summary/keywords and get best movie match
bot.hear(/summary (.*)/i, (payload, chat, data) => {
  const query =  String(data.match[1]);
  const summary = query.toLowerCase();
  // chat.say("I hear you.");
  // var test = new Packages.test();
  // System.out.println(test.tests("Interstellar"));
  // chat.say(test.tests("Interstellar"));

  // const movie = payload.message.text;
  // console.log(movie);

  java.callStaticMethod("Main", "getMovieGivenSummary", summary, function(err, results) {
    if (err) {
      console.error(err); return;
    }
    // do something with results
    chat.say("We recommend you watch " + results);
  
  });

});

bot.start(config.get('botPort'));
