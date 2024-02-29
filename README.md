### Code SQL to create the database (database name : chat_DB , if you want to give it another name change it also in src/main/java/err/anas/chatyou/dao/DBSingleton.java) : 

CREATE TABLE user (
  idUser int(11) PRIMARY KEY NOT NULL AUTO_INCREMENT,
  username varchar(50),
  password varchar(50)
);

CREATE TABLE message (
  idMsg int(11) PRIMARY KEY NOT NULL AUTO_INCREMENT,
  message varchar(191),
  idSender int(11),
  idReceiver int(11),
  CONSTRAINT fk_receiver FOREIGN KEY (idReceiver) REFERENCES user (idUser) ON DELETE CASCADE,
  CONSTRAINT fk_sender FOREIGN KEY (idSender) REFERENCES user (idUser) ON DELETE CASCADE
);

![image](https://github.com/anaserrami/Chat_Application/assets/103589151/577eaeec-3334-4756-b424-3e9f22232ddf)
