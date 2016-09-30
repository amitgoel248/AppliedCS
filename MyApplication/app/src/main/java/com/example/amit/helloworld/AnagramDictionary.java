package com.example.amit.helloworld;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

import static java.lang.Math.min;

public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 3;
    private static final int MAX_WORD_LENGTH = 7;
    private Random random = new Random();
    public int wordLength=DEFAULT_WORD_LENGTH;
    ArrayList<String> wordList = new ArrayList<String>();
    HashSet<String> wordSet = new HashSet<String>();
    HashMap<String,ArrayList<String> > letterToWord = new HashMap<String, ArrayList<String>>();
    HashMap<Integer,ArrayList<String> > sizeToWord = new HashMap<Integer, ArrayList<String>>();

    public AnagramDictionary(InputStream wordListStream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(wordListStream));
        String line;
        while((line = in.readLine()) != null) {
            String word = line.trim();
            wordSet.add(word);
            wordList.add(word);

            char[] temp = word.toCharArray();
            Arrays.sort(temp);
            String sorted_string = new String(temp);
            ArrayList<String> Temp_List = letterToWord.get(sorted_string);
            if(Temp_List!=null)
            {
                Temp_List.add(word);
            }
            else
            {
                ArrayList<String> New_list = new ArrayList<String>();
                New_list.add(word);
                letterToWord.put(sorted_string,New_list);
            }

            ArrayList<String> refactor_List = sizeToWord.get(word.length());
            if(refactor_List!=null)
            {
                refactor_List.add(word);
            }
            else
            {
                ArrayList<String> New_list = new ArrayList<String>();
                New_list.add(word);
                sizeToWord.put(word.length(),New_list);
            }

        }
    }

    public boolean isGoodWord(String word, String base) {
        if(wordSet.contains(word))
        {
            if(containSubstring(word,base))
            {
                return true;
            }
            else
                return false;
        }
        else
        return false;
    }

    private boolean containSubstring(String word, String base) {
        // Implement KMP
        return true;
    }

    public ArrayList<String> getAnagrams(String targetWord) {
        ArrayList<String> result = new ArrayList<String>();
        for(int i=0;i<wordList.size();i++)
        {
            String word = wordList.get(i);
            if(isAnagram(word,targetWord))
            {
                result.add(word);
            }
        }
        return result;
    }

    private boolean isAnagram(String word, String targetWord) {
        char[] c1 = word.toCharArray();
        Arrays.sort(c1);
        word = new String(c1);
        char[] c2 = targetWord.toCharArray();
        Arrays.sort(c2);
        targetWord = new String(c2);
        if(word.equals(targetWord))
            return true;
        else
            return false;
    }

    public ArrayList<String> getAnagramsWithOneMoreLetter(String word) {
        ArrayList<String> result = new ArrayList<String>();
        for(int i=0;i<26;i++)
        {
            String copy_string = word;
            char apnd = (char) (97+i);
            copy_string = copy_string + apnd;
            char[] temp = copy_string.toCharArray();
            Arrays.sort(temp);
            String temp_string  = new String(temp);
            ArrayList<String> copy_List = letterToWord.get(temp_string);
            if(copy_List!=null)
            {
                result.addAll(copy_List);
            }
        }
        return result;
    }

    public String pickGoodStarterWord()
    {
        Random rn = new Random();
        ArrayList<String> refactored_list = sizeToWord.get(wordLength);
        int i = rn.nextInt(refactored_list.size());
        String good_word = refactored_list.get(i);
        while(numberOfAnagrams(good_word)<MIN_NUM_ANAGRAMS)
        {
            i = rn.nextInt(refactored_list.size());
            good_word = refactored_list.get(i);
        }
        wordLength = min(wordLength+1,MAX_WORD_LENGTH);
        return good_word;
    }

    private int numberOfAnagrams(String good_word) {
        ArrayList<String> result = new ArrayList<String>();
        for(int i=0;i<26;i++)
        {
            String copy_string = good_word;
            char apnd = (char) (97+i);
            copy_string = copy_string + apnd;
            char[] temp = copy_string.toCharArray();
            Arrays.sort(temp);
            String temp_string  = new String(temp);
            ArrayList<String> copy_List = letterToWord.get(temp_string);
            if(copy_List!=null)
            {
                result.addAll(copy_List);
            }
        }
        return result.size();
    }
}
