package de.iteratec.logan.search;

import java.util.List;

import com.google.common.collect.Lists;


public class BoyerMoore {
  char[]  pattern;
  int[]   shiftTable;
  boolean ignoreCase;

  public BoyerMoore(String pat, boolean ignoreCase) {
    this(pat, 0x10000, ignoreCase);
  }

  public BoyerMoore(String pat, int tableSize, boolean ignoreCase) {
    this.pattern = pat.toCharArray();
    this.shiftTable = new int[tableSize];
    this.ignoreCase = ignoreCase;

    int length = pattern.length;
    for (int i = 0; i < this.shiftTable.length; i++)
      this.shiftTable[i] = length;

    for (int i = 0; i < length; i++) {
      char ch = this.pattern[i];
      int diff = length - i - 1;
      int index = ch % this.shiftTable.length;
      if (diff < this.shiftTable[index])
        this.shiftTable[index] = diff;
      if (this.ignoreCase) {
        ch = Character.toUpperCase(ch);
        index = ch % this.shiftTable.length;
        if (diff < this.shiftTable[index])
          this.shiftTable[index] = diff;
        ch = Character.toLowerCase(ch);
        index = ch % this.shiftTable.length;
        if (diff < this.shiftTable[index])
          this.shiftTable[index] = diff;
      }
    }
  }

  public List<Integer> findAll(CharSequence chars) {
    int length = chars.length();
    int start = 0;
    int match = -1;
    List<Integer> matches = Lists.newArrayList();
    while ((match = matches(chars, start, length)) > -1) {
      start = match + 1;
      matches.add(match);
    }

    return matches;
  }

  public int matches(CharSequence chars, int start, int limit) {
    if (this.ignoreCase)
      return this.matchesIgnoreCase(chars, start, limit);
    int plength = this.pattern.length;
    if (plength == 0)
      return start;
    int index = start + plength;
    while (index <= limit) {
      // System.err.println("Starts at "+index);
      int pindex = plength;
      int nindex = index + 1;
      char ch;
      do {
        if ((ch = chars.charAt(--index)) != this.pattern[--pindex])
          break;
        if (pindex == 0)
          return index;
      } while (pindex > 0);
      index += this.shiftTable[ch % this.shiftTable.length] + 1;
      if (index < nindex)
        index = nindex;
    }
    return -1;
  }

  int matchesIgnoreCase(CharSequence chars, int start, int limit) {
    int plength = this.pattern.length;
    if (plength == 0)
      return start;
    int index = start + plength;
    while (index <= limit) {
      int pindex = plength;
      int nindex = index + 1;
      char ch;
      do {
        char ch1 = ch = chars.charAt(--index);
        char ch2 = this.pattern[--pindex];
        if (ch1 != ch2) {
          ch1 = Character.toUpperCase(ch1);
          ch2 = Character.toUpperCase(ch2);
          if (ch1 != ch2 && Character.toLowerCase(ch1) != Character.toLowerCase(ch2))
            break;
        }
        if (pindex == 0)
          return index;
      } while (pindex > 0);
      index += this.shiftTable[ch % this.shiftTable.length] + 1;
      if (index < nindex)
        index = nindex;
    }
    return -1;
  }

}
