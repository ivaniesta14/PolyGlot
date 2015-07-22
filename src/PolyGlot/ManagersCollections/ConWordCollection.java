/*
 * Copyright (c) 2014-2015, Draque Thompson, draquemail@gmail.com
 * All rights reserved.
 *
 * Licensed under: Creative Commons Attribution-NonCommercial 4.0 International Public License
 * See LICENSE.TXT included with this code to read the full license agreement.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package PolyGlot.ManagersCollections;

import PolyGlot.Nodes.ConWord;
import PolyGlot.DictCore;
import PolyGlot.Nodes.DeclensionPair;
import PolyGlot.Nodes.DictNode;
import PolyGlot.PGTUtil;
import PolyGlot.Nodes.PronunciationNode;
import PolyGlot.Nodes.TypeNode;
import PolyGlot.RankedObject;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * @author draque
 *
 */
public class ConWordCollection extends DictionaryCollection {

    private final DictCore core;
    Map<String, Integer> allConWords;
    Map<String, Integer> allLocalWords;

    public ConWordCollection(DictCore _core) {
        bufferNode = new ConWord();
        allConWords = new HashMap<String, Integer>();
        allLocalWords = new HashMap<String, Integer>();
        core = _core;
    }

    /**
     * inserts current buffer word to conWord list based on id; blanks out
     * buffer
     *
     * @param _id
     * @return
     * @throws Exception
     */
    public Integer insert(Integer _id) throws Exception {
        Integer ret;

        ConWord insWord = new ConWord();
        insWord.setEqual(bufferNode);
        insWord.setId(_id);

        ((ConWord)bufferNode).setParent(this);
        ret = super.insert(_id, bufferNode);

        balanceWordCounts(insWord, true);

        bufferNode = new ConWord();

        return ret;
    }

    /**
     * inserts current buffer to conWord list and generates id; blanks out
     * buffer
     *
     * @return ID of newly created node
     * @throws Exception
     */
    public Integer insert() throws Exception {
        Integer ret;
        
        ((ConWord)bufferNode).setParent(this);
        ret = super.insert(bufferNode);

        balanceWordCounts((ConWord) bufferNode, true);

        bufferNode = new ConWord();

        return ret;
    }
    
    /**
     * Gets count of conwords in dictionary
     * @return number of conwords in dictionary
     */
    public int getWordCount() {
        return nodeMap.size();
    }

    /**
     * Tests whether collection contains a particular local word
     *
     * @param local string value to search for
     * @return whether multiples of local word exists in collection
     */
    public boolean containsLocalMultiples(String local) {
        boolean ret = false;

        if (allLocalWords.containsKey(local)) {
            ret = allLocalWords.get(local) > 1;
        }

        return ret;
    }

    /**
     * Tests whether collection contains a particular conword
     *
     * @param word string value to search for
     * @return whether multiples of conword exists in the collection
     */
    public boolean containsWord(String word) {
        boolean ret = false;

        if (allConWords.containsKey(word)) {
            ret = allConWords.get(word) > 1;
        }

        return ret;
    }

    /**
     * Balances count of conwords and localwords (string values)
     *
     * @param insWord word to factor into counts
     * @param additive true if adding, false if removing
     */
    private void balanceWordCounts(ConWord insWord, boolean additive) {
        Integer curCount = allConWords.containsKey(insWord.getValue())
                ? allConWords.get(insWord.getValue()) : 0;
        allConWords.remove(insWord.getValue());
        allConWords.put(insWord.getValue(), curCount + (additive ? 1 : -1));

        curCount = allLocalWords.containsKey(insWord.getLocalWord())
                ? allLocalWords.get(insWord.getLocalWord()) : 0;
        allLocalWords.remove(insWord.getLocalWord());
        allLocalWords.put(insWord.getLocalWord(), curCount + (additive ? 1 : -1));
    }

    /**
     * Balances word counts when modifying word value or local word MUST BE RUN
     * BEFORE PERSISTING NEW VALUES TO WORD
     *
     * @param id id of word to modify
     * @param wordVal new conword value
     * @param wordLoc new local word value
     * @throws java.lang.Exception if word not found
     */
    public void extertalBalanceWordCounts(Integer id, String wordVal, String wordLoc) throws Exception {
        ConWord oldWord = getNodeById(id);
        ConWord newWord = new ConWord();

        newWord.setValue(wordVal);
        newWord.setLocalWord(wordLoc);

        balanceWordCounts(oldWord, false);
        balanceWordCounts(newWord, true);
    }
    
    /**
     * Tests whether a value exists in the dictionary currently
     * @param word value to search for
     * @return true if exists, false otherwise
     */
    public boolean testWordValueExists(String word) {
        return allConWords.containsKey(word) && allConWords.get(word) > 0;
    }
    
    /**
     * Tests whether a value exists in the dictionary currently
     * @param local value to search for
     * @return true if exists, false otherwise
     */
    public boolean testLocalValueExists(String local) {
        return allLocalWords.containsKey(local) && allLocalWords.get(local) > 0;
    }

    /**
     * WARNING: DO NOT CALL THIS OUTSIDE OF DICT CORE: CLEANUP DONE THERE
     *
     * @param _id ID of word to delete
     * @throws Exception
     */
    @Override
    public void deleteNodeById(Integer _id) throws Exception {
        ConWord deleteWord = this.getNodeById(_id);

        balanceWordCounts(deleteWord, false);

        super.deleteNodeById(_id);
    }

    @Override
    public void modifyNode(Integer _id, DictNode _modNode) throws Exception {
        // do bookkeepingfor word counts
        ConWord oldWord = getNodeById(_id);
        balanceWordCounts(oldWord, false);
        balanceWordCounts((ConWord) _modNode, true);
        ((ConWord) _modNode).setCore(core);

        super.modifyNode(_id, _modNode);
    }

    /**
     * Performs all actions of superclass, and additionally sets core value of
     * words
     *
     * @param _id same as super
     * @param _buffer same as super
     * @return same as super
     * @throws Exception same as super
     */
    @Override
    protected Integer insert(Integer _id, DictNode _buffer) throws Exception {
        ((ConWord) _buffer).setCore(core);
        ((ConWord)_buffer).setParent(this);
        return super.insert(_id, _buffer);
    }

    /**
     * recalculates all non-overridden pronunciations
     *
     * @throws java.lang.Exception
     */
    public void recalcAllProcs() throws Exception {
        Iterator<ConWord> it = this.getNodeIterator();

        while (it.hasNext()) {
            ConWord curWord = it.next();

            // only runs if word's pronunciation not overridden
            if (!curWord.isProcOverride()) {
                curWord.setPronunciation(core.getPronunciationMgr().getPronunciation(curWord.getValue()));
                this.modifyNode(curWord.getId(), curWord);
            }
        }
    }

    /**
     * Returns list of words in descending list of synonym match
     *
     * @param _match The string value to match for
     * @return list of matching words
     */
    public List<ConWord> getSuggestedTransWords(String _match) {
        List<ConWord> localEquals = new ArrayList<ConWord>();
        List<ConWord> localContains = new ArrayList<ConWord>();
        List<RankedObject> definitionContains = new ArrayList<RankedObject>();
        Iterator<Entry<Integer, ConWord>> allWords = nodeMap.entrySet().iterator();

        // on empty, return empty list
        if (_match.equals("")) {
            return localEquals;
        }

        Entry<Integer, ConWord> curEntry;
        ConWord curWord;

        // cycles through all words, searching for matches
        while (allWords.hasNext()) {
            curEntry = allWords.next();
            curWord = curEntry.getValue();

            String word = curWord.getValue();
            String compare = _match;
            String definition = curWord.getDefinition();

            // on ignore case, force all to lowercase
            if (core.getPropertiesManager().isIgnoreCase()) {
                word = word.toLowerCase();
                compare = compare.toLowerCase();
                definition = definition.toLowerCase();
            }

            if (word.equals(compare)) {
                // local word equility is the highest ranking match
                localEquals.add(curWord);
            } else if (word.contains(compare)) {
                // local word contains value is the second highest ranking match
                localContains.add(curWord);
            } else if (definition.contains(compare)) {
                // definition contains is ranked third, and itself raked inernally
                // by match position
                definitionContains.add(new RankedObject(curWord, definition.indexOf(compare)));
            }
        }

        Collections.sort(definitionContains);

        // concatinate results
        ArrayList<ConWord> ret = new ArrayList<ConWord>();
        ret.addAll(localEquals);
        ret.addAll(localContains);

        // must add through iteration here
        Iterator<RankedObject> it = definitionContains.iterator();
        while (it.hasNext()) {
            RankedObject curObject = it.next();
            ConWord curDefMatch = (ConWord) curObject.getHolder();

            ret.add(curDefMatch);
        }

        return ret;
    }

    /**
     * Uses conword passed as parameter to filter on the entire dictionary of
     * words, based on attributes set on the parameter. Returns iterator of all
     * words that match. As a note: the conword value of the filter parameter is
     * matched not only against the values of all conwords in the dictionary,
     * but also their conjugations/declensions
     *
     * @param _filter A conword object containing filter values
     * @return an iterator of conwords which match the given search
     * @throws Exception on filtering error
     */
    public Iterator<ConWord> filteredList(ConWord _filter) throws Exception {
        ConWordCollection retValues = new ConWordCollection(core);
        retValues.setAlphaOrder(alphaOrder);

        Iterator<Entry<Integer, ConWord>> filterList = nodeMap.entrySet()
                .iterator();
        Entry<Integer, ConWord> curEntry;
        ConWord curWord;

        // set filter to lowercase if ignoring case
        if (core.getPropertiesManager().isIgnoreCase()) {
            _filter.setDefinition(_filter.getDefinition().toLowerCase());
            _filter.setWordType(_filter.getWordType().toLowerCase());
            _filter.setLocalWord(_filter.getLocalWord().toLowerCase());
            _filter.setValue(_filter.getValue().toLowerCase());
            _filter.setGender(_filter.getGender().toLowerCase());
            _filter.setPronunciation(_filter.getPronunciation().toLowerCase());
        }

        while (filterList.hasNext()) {
            curEntry = filterList.next();
            curWord = curEntry.getValue();
            try {
                String definition;
                String type;
                String local;
                String gender;
                String proc;

                // if set to ignore case, set up caseless matches, normal otherwise
                if (core.getPropertiesManager().isIgnoreCase()) {
                    definition = curWord.getDefinition().toLowerCase();
                    type = curWord.getWordType().toLowerCase();
                    local = curWord.getLocalWord().toLowerCase();
                    gender = curWord.getGender().toLowerCase();
                    proc = curWord.getPronunciation().toLowerCase();
                } else {
                    definition = curWord.getDefinition();
                    type = curWord.getWordType();
                    local = curWord.getLocalWord();
                    gender = curWord.getGender();
                    proc = curWord.getPronunciation();
                }

                // each filter test split up to minimize compares                
                // definition
                if (!_filter.getDefinition().trim().equals("")
                        && !definition.contains(
                                _filter.getDefinition())) {
                    continue;
                }

                // type (exact match only)
                if (!_filter.getWordType().trim().equals("")
                        && !type.equals(_filter.getWordType())) {
                    continue;
                }

                // local word
                if (!_filter.getLocalWord().trim().equals("")
                        && !(local.matches(_filter.getLocalWord())
                        || local.startsWith(_filter.getLocalWord()))) {
                    continue;
                }

                // con word
                if (!matchHeadAndDeclensions(_filter.getValue(), curWord)) {
                    continue;
                }

                // gender (exact match only)
                if (!_filter.getGender().trim().equals("")
                        && !gender.equals(_filter.getGender())) {
                    continue;
                }

                // pronunciation
                if (!_filter.getPronunciation().trim().equals("")
                        && !proc.contains(_filter.getPronunciation())) {
                    continue;
                }

                retValues.setBufferWord(curWord);
                retValues.insert(curWord.getId());
            } catch (Exception e) {
                throw new Exception("FILTERING ERROR: " + e.getMessage());
            }

        }

        return retValues.getNodeIterator();
    }

    /**
     * Tests whether matchText matches the headword of the passed word, or any
     * declensions/conjugations of the word.
     *
     * @param matchText Text to match.
     * @param word Word within which to search for matches
     * @return true if match, false otherwise
     */
    private boolean matchHeadAndDeclensions(String matchText, ConWord word) {
        boolean ret = false;
        boolean ignoreCase = core.getPropertiesManager().isIgnoreCase();

        String head = ignoreCase ? word.getValue().toLowerCase() : word.getValue();

        if (matchText.trim().isEmpty() 
                || head.matches(matchText) 
                || head.startsWith(matchText)) {
            ret = true;
        }
        TypeNode type = core.getTypes().findTypeByName(word.getWordType());

        if (type != null && ! ret) {
            int typeId = type.getId();
            Iterator<DeclensionPair> decIt = core.getDeclensionManager().getAllCombinedIds(typeId).iterator();

            while (!ret && decIt.hasNext()) {
                DeclensionPair curPair = decIt.next();
                String declension = core.getDeclensionManager()
                        .declineWord(typeId, curPair.combinedId, word.getValue());

                if (!declension.trim().isEmpty() 
                        && (declension.matches(matchText) 
                            || declension.startsWith(matchText))) {
                    ret = true;
                }
            }
        }
        
        return ret;
    }

    @Override
    public ConWord getNodeById(Integer _id) throws Exception {
        return (ConWord) super.getNodeById(_id);
    }

    /**
     * wipes current word buffer
     */
    @Override
    public void clear() {
        bufferNode = new ConWord();
    }

    public ConWord getBufferWord() {
        return (ConWord) bufferNode;
    }

    public void setBufferWord(ConWord bufferWord) {
        this.bufferNode = bufferWord;
    }

    /**
     * returns iterator of nodes with their IDs as the entry key (ordered)
     *
     * @return
     */
    public Iterator<ConWord> getNodeIterator() {
        List<ConWord> retList = new ArrayList<ConWord>(nodeMap.values());

        Collections.sort(retList);

        return retList.iterator();
    }

    /**
     * Inserts new word into dictionary
     *
     * @param _addWord word to be inserted
     * @return ID of newly inserted word
     * @throws Exception
     */
    public int addWord(ConWord _addWord) throws Exception {
        int ret;
        bufferNode.setEqual(_addWord);

        ret = insert();

        return ret;
    }

    /**
     * Builds report on words in ConLang. Potentially computationally expensive.
     *
     * @return
     */
    public String buildWordReport() {
        String ret = "";

        Map<String, Integer> wordStart = new HashMap<String, Integer>();
        Map<String, Integer> wordEnd = new HashMap<String, Integer>();
        Map<String, Integer> characterCombos2 = new HashMap<String, Integer>();
        Integer highestCombo2 = 0;
        Map<String, Integer> characterCombos3 = new HashMap<String, Integer>();
        Map<String, Integer> typeCountByWord = new HashMap<String, Integer>();
        Map<String, Integer> phonemeCount = new HashMap<String, Integer>();
        Map<String, Integer> charCount = new HashMap<String, Integer>();
        Map<String, Integer> phonemeCombo2 = new HashMap<String, Integer>();
        Integer wordCount = nodeMap.size();

        Iterator<ConWord> wordIt = new ArrayList<ConWord>(nodeMap.values()).iterator();

        // Put values into maps to count/record... 
        while (wordIt.hasNext()) {
            ConWord curWord = wordIt.next();
            final String curValue = curWord.getValue();
            final int curValueLength = curValue.length();
            final String curType = curWord.getWordType();

            String beginsWith = curValue.substring(0, 1);
            String endsWith = curValue.substring(curValueLength - 1, curValueLength);

            // either increment or create value for starting character
            if (wordStart.containsKey(beginsWith)) {
                int newValue = wordStart.get(beginsWith) + 1;
                wordStart.remove(beginsWith);
                wordStart.put(beginsWith, newValue);
            } else {
                wordStart.put(beginsWith, 1);
            }

            // either increment or create value for ending character
            if (wordEnd.containsKey(endsWith)) {
                int newValue = wordEnd.get(endsWith) + 1;
                wordEnd.remove(endsWith);
                wordEnd.put(endsWith, newValue);
            } else {
                wordEnd.put(endsWith, 1);
            }

            // capture and record all phonemes in word and phoneme combinations
            List<PronunciationNode> phonArray = core.getPronunciationMgr()
                    .getPronunciationElements(curValue);

            for (int i = 0; i < phonArray.size(); i++) {
                if (phonemeCount.containsKey(phonArray.get(i).getPronunciation())) {
                    int newValue = phonemeCount.get(phonArray.get(i).getPronunciation()) + 1;
                    phonemeCount.remove(phonArray.get(i).getPronunciation());
                    phonemeCount.put(phonArray.get(i).getPronunciation(), newValue);
                } else {
                    phonemeCount.put(phonArray.get(i).getPronunciation(), 1);
                }

                // grab combo if there are additinal phonemes, otherwise you're done
                if (i + 1 < phonArray.size()) {
                    String curCombo = phonArray.get(i).getPronunciation() + " "
                            + phonArray.get(i + 1).getPronunciation();

                    if (phonemeCombo2.containsKey(curCombo)) {
                        int newValue = phonemeCombo2.get(curCombo) + 1;
                        phonemeCombo2.remove(curCombo);
                        phonemeCombo2.put(curCombo, newValue);
                    } else {
                        phonemeCombo2.put(curCombo, 1);
                    }
                }
            }

            // caupture all individual characters
            for (int i = 0; i < curValueLength; i++) {
                String curChar = curValue.substring(i, i + 1);

                if (charCount.containsKey(curChar)) {
                    int newValue = charCount.get(curChar) + 1;
                    charCount.remove(curChar);
                    charCount.put(curChar, newValue);
                } else {
                    charCount.put(curChar, 1);
                }
            }

            // capture and record all 2 character combinations in words
            for (int i = 0; i < curValueLength - 1; i++) {
                String combo = curValue.substring(i, i + 2);

                if (characterCombos2.containsKey(combo)) {
                    int curComboCount = characterCombos2.get(combo);

                    if (highestCombo2 <= curComboCount) {
                        highestCombo2 = curComboCount + 1;
                    }

                    int newValue = characterCombos2.get(combo) + 1;
                    characterCombos2.remove(combo);
                    characterCombos2.put(combo, newValue);
                } else {
                    characterCombos2.put(combo, 1);
                }
            }

            // capture and record all 3 character combinations in words
            for (int i = 0; i < curValueLength - 2; i++) {
                String combo = curValue.substring(i, i + 3);

                if (characterCombos3.containsKey(combo)) {
                    int newValue = characterCombos3.get(combo) + 1;
                    characterCombos3.remove(combo);
                    characterCombos3.put(combo, newValue);
                } else {
                    characterCombos3.put(combo, 1);
                }
            }

            // record type count...
            if (typeCountByWord.containsKey(curType)) {
                int newValue = typeCountByWord.get(curType) + 1;
                typeCountByWord.remove(curType);
                typeCountByWord.put(curType, newValue);
            } else {
                typeCountByWord.put(curType, 1);
            }
        }

        ret += formatPlain("Count of words in conlang lexicon: " + wordCount + "<br><br>");

        // build display of type counts
        ret += formatPlain("count of words by type:<br>");
        for (Entry<String, Integer> curEntry : typeCountByWord.entrySet()) {
            ret += formatPlain(curEntry.getKey() + " : " + curEntry.getValue() + "<br>");
        }
        ret += formatPlain("<br><br>");

        // build display for starts-with statistics
        ret += formatPlain(" Breakdown of words counted starting with letter:<br>");
        for (char letter : core.getPropertiesManager().getAlphaPlainText().toCharArray()) {
            ret += letter + formatPlain(" : "
                    + (wordStart.containsKey("" + letter) ? wordStart.get("" + letter) : formatPlain("0")) + "<br>");
        }
        ret += formatPlain("<br><br>");

        // build display for ends-with statistics
        ret += formatPlain(" Breakdown of words counted ending with letter:<br>");
        for (char letter : core.getPropertiesManager().getAlphaPlainText().toCharArray()) {
            ret += letter + formatPlain(" : "
                    + (wordEnd.containsKey("" + letter) ? wordEnd.get("" + letter) : formatPlain("0")) + "<br>");
        }
        ret += formatPlain("<br><br>");

        // build display for character counts
        ret += formatPlain(" Breakdown of characters counted across all words:<br>");
        for (char letter : core.getPropertiesManager().getAlphaPlainText().toCharArray()) {
            ret += letter + formatPlain(" : "
                    + (charCount.containsKey("" + letter) ? charCount.get("" + letter) : formatPlain("0")) + "<br>");
        }
        ret += formatPlain("<br><br>");

        // build display for phoneme count
        ret += formatPlain(" Breakdown of phonemes counted across all words:<br>");
        Iterator<PronunciationNode> procLoop = core.getPronunciationMgr().getPronunciations();
        while (procLoop.hasNext()) {
            PronunciationNode curNode = procLoop.next();
            ret += formatPlain(curNode.getPronunciation() + " : "
                    + (phonemeCount.containsKey(curNode.getPronunciation())
                            ? phonemeCount.get(curNode.getPronunciation()) : formatPlain("0")) + "<br>");
        }
        ret += formatPlain("<br><br>");

        // buid grid of 2 letter combos
        ret += formatPlain("Heat map of letter combination frequency:<br>");
        ret += "<table border=\"1\">";
        ret += "<tr><td></td>";
        for (char topRow : core.getPropertiesManager().getAlphaPlainText().toCharArray()) {
            ret += "<td>" + topRow + "</td>";
        }
        ret += "</tr>";
        for (char y : core.getPropertiesManager().getAlphaPlainText().toCharArray()) {
            ret += "<tr><td>" + y + "</td>";
            for (char x : core.getPropertiesManager().getAlphaPlainText().toCharArray()) {
                String search = "" + x + y;
                Integer comboValue = (characterCombos2.containsKey(search)
                        ? characterCombos2.get(search) : 0);

                Integer red = (255 / highestCombo2) * comboValue;
                Integer blue = 255 - red;
                ret += "<td bgcolor=rgb(" + red + "," + blue + "," + blue + ")>"
                        + x + y + formatPlain(":"
                                + comboValue.toString()) + "</td>";
            }
            ret += "</tr>";
        }
        ret += "</table>" + formatPlain("<br><br>");

        // buid grid of 2 phoneme combos
        ret += formatPlain("Heat map of phoneme combination frequency:<br>");
        ret += "<table border=\"1\">";
        ret += "<tr>" + formatPlain("<td></td>");
        Iterator<PronunciationNode> procIty = core.getPronunciationMgr().getPronunciations();
        while (procIty.hasNext()) {
            ret += "<td>" + formatPlain(formatPlain(procIty.next().getPronunciation())) + "</td>";
        }
        ret += "</tr>";
        procIty = core.getPronunciationMgr().getPronunciations();
        while (procIty.hasNext()) {
            PronunciationNode y = procIty.next();
            ret += "<tr><td>" + formatPlain(y.getPronunciation()) + "</td>";
            Iterator<PronunciationNode> procItx = core.getPronunciationMgr().getPronunciations();
            while (procItx.hasNext()) {
                PronunciationNode x = procItx.next();
                String search = x.getPronunciation() + " " + y.getPronunciation();
                Integer comboValue = (phonemeCombo2.containsKey(search)
                        ? phonemeCombo2.get(search) : 0);

                Integer red = (255 / highestCombo2) * comboValue;
                Integer blue = 255 - red;
                ret += "<td bgcolor=rgb(" + red + "," + blue + "," + blue + ")>"
                        + formatPlain(x.getPronunciation() + y.getPronunciation() + ":"
                                + comboValue.toString()) + "</td>";
            }
            ret += "</tr>";
        }
        ret += "</table>";

        return ret;
    }

    /**
     * Formats in HTML to a plain font to avoid conlang font
     *
     * @param toPlain text to make plain
     * @return text in plain tag
     */
    public static String formatPlain(String toPlain) {
        String defaultFont = "face=\"" + Font.SANS_SERIF + "\"";
        return "<font " + defaultFont + ">" + toPlain + "</font>";
    }

    /**
     * Writes all word information to XML document
     *
     * @param doc Document to write to
     * @param rootElement root element of document
     */
    public void writeXML(Document doc, Element rootElement) {
        Iterator<ConWord> wordLoop = getNodeIterator();
        Element wordNode;
        Element wordValue;
        ConWord curWord;

        while (wordLoop.hasNext()) {
            curWord = wordLoop.next();

            wordNode = doc.createElement(PGTUtil.wordXID);
            rootElement.appendChild(wordNode);

            wordValue = doc.createElement(PGTUtil.wordIdXID);
            Integer wordId = curWord.getId();
            wordValue.appendChild(doc.createTextNode(wordId.toString()));
            wordNode.appendChild(wordValue);

            wordValue = doc.createElement(PGTUtil.localWordXID);
            wordValue.appendChild(doc.createTextNode(curWord.getLocalWord()));
            wordNode.appendChild(wordValue);

            wordValue = doc.createElement(PGTUtil.conWordXID);
            wordValue.appendChild(doc.createTextNode(curWord.getValue()));
            wordNode.appendChild(wordValue);

            wordValue = doc.createElement(PGTUtil.wordTypeXID);
            wordValue.appendChild(doc.createTextNode(curWord.getWordType()));
            wordNode.appendChild(wordValue);

            wordValue = doc.createElement(PGTUtil.pronunciationXID);
            wordValue
                    .appendChild(doc.createTextNode(curWord.getPronunciation()));
            wordNode.appendChild(wordValue);

            wordValue = doc.createElement(PGTUtil.wordGenderXID);
            wordValue.appendChild(doc.createTextNode(curWord.getGender()));
            wordNode.appendChild(wordValue);

            wordValue = doc.createElement(PGTUtil.definitionXID);
            wordValue.appendChild(doc.createTextNode(curWord.getDefinition()));
            wordNode.appendChild(wordValue);

            wordValue = doc.createElement(PGTUtil.wordProcOverrideXID);
            wordValue.appendChild(doc.createTextNode(curWord.isProcOverride() ? "T" : "F"));
            wordNode.appendChild(wordValue);

            wordValue = doc.createElement(PGTUtil.wordAutoDeclenOverrideXID);
            wordValue.appendChild(doc.createTextNode(curWord.isOverrideAutoDeclen() ? "T" : "F"));
            wordNode.appendChild(wordValue);

            wordValue = doc.createElement(PGTUtil.wordRuleOverrideXID);
            wordValue.appendChild(doc.createTextNode(curWord.isRulesOverrride() ? "T" : "F"));
            wordNode.appendChild(wordValue);
        }
    }
}