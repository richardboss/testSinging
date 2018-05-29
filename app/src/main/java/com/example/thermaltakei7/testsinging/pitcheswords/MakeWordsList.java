package com.example.thermaltakei7.testsinging.pitcheswords;

import java.util.ArrayList;
import java.util.List;

/**
 * Gets the whole Pitch List and generates word List from it.
 * The logic is that human ear can not hear pitches higher than 10 000, so if the pitch
 * is higher than that for some time then that means the word has ended
 */
public class MakeWordsList {

    List<Pitch> pitches;
    List<Pitch> oneWord = new ArrayList<>();
    List<List<Pitch>> wordList = new ArrayList<>();

    public List<List<Pitch>> getWordsList(List<Pitch> pitchList) {
        return makeWordsList(pitchList);
    }

    public List<List<Pitch>> makeWordsList(List<Pitch> pitchList) {
        pitches = pitchList;
        int pauseCounter = 0;

        for (int i = 0; i < pitchList.size(); i++) {
            if (pitchList.get(i).getPitch() > 10000) {
                pauseCounter++;
            } else {

                if (pauseCounter > 0) {
                    if (pauseCounter >= 5) {
                        wordList.add(oneWord);
                        oneWord = new ArrayList<>();
                    }
                    pauseCounter = 0;
                }
                oneWord.add(pitchList.get(i));
            }
        }
        if (oneWord.size() > 0) {
            wordList.add(oneWord);
        }
        return wordList;
    }
}
