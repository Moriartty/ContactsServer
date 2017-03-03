package Emotion_analysis;

import DB.PutInDB;
import DB.getData;

public class SentimentAnalyseTest {
	public void writeInDB(String personName){
		PutInDB.InsertSentimentScore(personName);
	}

}
