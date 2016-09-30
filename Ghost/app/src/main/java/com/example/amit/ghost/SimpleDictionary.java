package com.example.amit.ghost;

import java.io.BufferedReader;
import java.io.CharArrayReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

public class SimpleDictionary implements GhostDictionary {
    private ArrayList<String> words;

    public SimpleDictionary(InputStream wordListStream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(wordListStream));
        words = new ArrayList<>();
        String line = null;
        while((line = in.readLine()) != null) {
            String word = line.trim();
            if (word.length() >= MIN_WORD_LENGTH)
              words.add(line.trim());
        }
    }

    @Override
    public boolean isWord(String word) {
        return words.contains(word);
    }

    @Override
    public String getAnyWordStartingWith(String prefix) {
        if(prefix.equals(""))
        {
            Random rn = new Random();
            int index = rn.nextInt(words.size()-1);
            String start = words.get(index);
            return start;
        }
        else
        {
            int left = 0;
            int right = words.size()-1;
            while(left<right)
            {
                int mid = (left+right)/2;
                int chk = prefix_function(prefix,words.get(mid));
                if(chk==0)
                {
                    return words.get(mid);
                }
                else if(chk==1)
                {
                    left=mid+1;
                }
                else
                    right=mid-1;
            }
            return null;
        }
    }

    private int prefix_function(String prefix, String s) {
        if(s.length()<prefix.length())
        {
            if(s.compareTo(prefix)<0)
            {
                return 1;
            }
            else
                return 0;
        }
        else
        {
            int len = prefix.length();
            String temp = s.substring(0,len);
            int val = prefix.compareTo(temp);
            if(val==0)
                return 0;
            else if(val>0)
                return 1;
            else
                return -1;
        }
    }

    @Override
    public String getGoodWordStartingWith(String prefix) {
        return null;
    }
}
