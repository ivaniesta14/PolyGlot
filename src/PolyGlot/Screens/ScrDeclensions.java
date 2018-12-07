/*
 * Copyright (c) 2014-2018, Draque Thompson, draquemail@gmail.com
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
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package PolyGlot.Screens;

import PolyGlot.Nodes.ConWord;
import PolyGlot.Nodes.DeclensionNode;
import PolyGlot.Nodes.DeclensionPair;
import PolyGlot.DictCore;
import PolyGlot.CustomControls.InfoBox;
import PolyGlot.CustomControls.PButton;
import PolyGlot.CustomControls.PDialog;
import PolyGlot.Nodes.TypeNode;
import PolyGlot.CustomControls.PTextField;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Toolkit;
import java.awt.Window;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

/**
 *
 * @author draque
 */
public class ScrDeclensions extends PDialog {

    private final Map<String, JTextField> fieldMap = new HashMap<>();
    private final Map<String, String> labelMap = new HashMap<>();
    private ConWord word;
    private Integer typeId;
    private Font conFont;
    private Integer numFields = 0;
    private Integer textHeight = 0;
    private Map<String, DeclensionNode> allWordDeclensions = new HashMap<>();
    private JTextField firstField;
    private final Integer MAXLABELWIDTH = 300;

    public ScrDeclensions(DictCore _core) {
        core = _core;
        this.setupKeyStrokes();
        initComponents();
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setModal(true);
    }
    
    @Override
    public final void setModal(boolean _modal) {
        super.setModal(_modal);
    }
    
    @Override 
    public final void setDefaultCloseOperation(int _operation) {
        super.setDefaultCloseOperation(_operation);
    }
    
    @Override 
    public final void setupKeyStrokes() {
        super.setupKeyStrokes();
    }

    @Override
    public void updateAllValues(DictCore _core) {
        // No values to update due to modal nature of window
    }
    
    public void setConWord(ConWord _word) {
        word = _word;
    }

    public void setWordType(Integer _typeId) {
        typeId = _typeId;
    }

    public void setConFont(Font _conFont) {
        conFont = _conFont;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnCancel = new PButton(core);
        btnOk = new PButton(core);
        scrDeclensions = new javax.swing.JScrollPane();
        pnlDeclensions = new javax.swing.JPanel();
        chkAutogenOverride = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Conjugations/Declensions");

        btnCancel.setText("Cancel");
        btnCancel.setToolTipText("Exit screen without saving anything.");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        btnOk.setText("OK");
        btnOk.setToolTipText("Accept and save changes to declension/conjugations.");
        btnOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOkActionPerformed(evt);
            }
        });

        pnlDeclensions.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout pnlDeclensionsLayout = new javax.swing.GroupLayout(pnlDeclensions);
        pnlDeclensions.setLayout(pnlDeclensionsLayout);
        pnlDeclensionsLayout.setHorizontalGroup(
            pnlDeclensionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 344, Short.MAX_VALUE)
        );
        pnlDeclensionsLayout.setVerticalGroup(
            pnlDeclensionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 441, Short.MAX_VALUE)
        );

        scrDeclensions.setViewportView(pnlDeclensions);

        chkAutogenOverride.setText("Autogen Override");
        chkAutogenOverride.setToolTipText("Check to override autogeneration of declension forms (if autogeneration patterns exist)");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(chkAutogenOverride)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnOk)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnCancel)
                .addContainerGap())
            .addComponent(scrDeclensions)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(scrDeclensions)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnOk, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(chkAutogenOverride))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        this.dispose();
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOkActionPerformed
        saveDeclension();
    }//GEN-LAST:event_btnOkActionPerformed

    public static Window run(DictCore _core, ConWord _word) {
        TypeNode wordType = _core.getTypes().getNodeById(_word.getWordTypeId());
        
        final ScrDeclensions s = new ScrDeclensions(_core);
        s.setConWord(_word);
        s.setConFont(_core.getPropertiesManager().getFontCon());
        s.setWordType(wordType == null ? -1 : wordType.getId());
        s.getAllWordDeclensions();
        s.buildForm();        
        s.setModal(true);

        // set up screen after it has been built (in setVisible)
        SwingUtilities.invokeLater(s::setFormProps);
        s.setCore(_core);
        s.setVisible(true);
        
        return s;
    }
    
    @Override
    public void setVisible(boolean visible) {

        if (typeId == -1) {
            InfoBox.info("Missing Part of Speech", "Word must have a part of Speech set, and declensions defined before using this feature.", core.getRootWindow());
            this.dispose();
        } else if ((core.getDeclensionManager().getDimensionalDeclensionListTemplate(typeId) == null
                    || core.getDeclensionManager().getDimensionalDeclensionListTemplate(typeId).isEmpty())
                && core.getDeclensionManager().getDeclensionListWord(word.getId()).isEmpty()) {
            InfoBox.info("Declensions", "No declensions for part of speech: " + word.getWordTypeDisplay()
                    + " set. Declensions can be created per part of speech under the Part of Speech menu by clicking the Declensions button.", core.getRootWindow());

            this.dispose();
        } else {
            super.setVisible(visible);
        }
    }

    private void saveDeclension() {
        word.setOverrideAutoDeclen(chkAutogenOverride.isSelected());
        
        core.clearAllDeclensionsWord(word.getId());
        Set<Entry<String, JTextField>> saveSet = fieldMap.entrySet();

        saveSet.stream().filter((entry) -> entry.getValue().getText().trim().length() != 0)
        .forEach((entry) -> {
            DeclensionNode saveNode = new DeclensionNode(-1);
            String curId = entry.getKey();

            saveNode.setValue(entry.getValue().getText().trim());
            saveNode.setCombinedDimId(curId);
            saveNode.setNotes(labelMap.get(curId));

            // declensions per word not saved via int id any longer
            core.getDeclensionManager().addDeclensionToWord(word.getId(), -1, saveNode);
        });

        dispose();
    }

    /**
     * creates all the fields for declension
     * combinations
     */
    private void createFields() {
        Iterator<DeclensionPair> decIt = core.getDeclensionManager().getAllCombinedIds(typeId).iterator();
        
        while (decIt.hasNext()) {
            DeclensionPair curDec = decIt.next();
            String curId = curDec.combinedId;
            String curLabel = curDec.label;
            
            // skip forms that have been surpressed
            if (core.getDeclensionManager().isCombinedDeclSurpressed(curId, typeId)) {
                allWordDeclensions.remove(curId);
                continue;
            }
            
            Label newLabel = new Label(curLabel);
            JTextField newField = new PTextField(core, false, "");
            Dimension labelDim = new Dimension();
            labelDim.setSize(MAXLABELWIDTH, 0);
            newLabel.setMaximumSize(labelDim);
            
            DeclensionNode findDec = core.getDeclensionManager().getDeclensionByCombinedId(word.getId(), curId);

            if (findDec != null) {
                String value = findDec.getValue();
                newField.setText(value);
                try {
                    newField.setToolTipText(core.getPronunciationMgr().getPronunciation(value));
                } catch (Exception e) {
                    newField.setToolTipText("Regex error: " + e.getLocalizedMessage());
                }
            }
            
            // if the autodeclension override is not set, create a value
            if (!word.isOverrideAutoDeclen()) {
                try {
                    String newForm = core.getDeclensionManager().declineWord(word, curId, word.getValue());
                    
                    // only set value if form found
                    if (newForm.length() != 0) {
                        newField.setText(newForm);
                    }
                } catch (Exception e) {
                    InfoBox.error("Declension Error", e.getLocalizedMessage(), this);
                }
            }

            if (conFont != null) {
                newField.setFont(conFont);
            }

            pnlDeclensions.add(newLabel);
            pnlDeclensions.add(newField);
            allWordDeclensions.remove(curId);

            // capture first field to pull height from later...
            if (firstField == null) {
                firstField = newField;
            }

            fieldMap.put(curId, newField);
            labelMap.put(curId, curLabel);
            numFields++;
        }
    }
    
    /**
     * creates fields for deprecated dimension combinations
     */
    private void createDeprecatedFields() {
        Set<Entry<String, DeclensionNode>> decSet = allWordDeclensions.entrySet();
        Iterator<Entry<String, DeclensionNode>> depIt = decSet.iterator();

        // separates new and deprecated fields
        if (!decSet.isEmpty()) {
            pnlDeclensions.add(new Label("DEPRECATED DECLENSIONS"));
            pnlDeclensions.add(new Label(" "));
            numFields ++;
        }
        
        while (depIt.hasNext()) {
            Entry<String, DeclensionNode> decEnt = depIt.next();
            DeclensionNode curDec = decEnt.getValue();

            JTextField newField = new PTextField(core, false, "");
            Label newLabel = new Label(curDec.getNotes());
            
            // in the case of no patterns for word type, but existing deprecated declensions
            if (firstField == null) {
                firstField = newField;
            }

            String value = curDec.getValue();
            newField.setText(value);
            try {
                newField.setToolTipText(core.getPronunciationMgr()
                        .getPronunciation(value));
            } catch (Exception e) {
                newField.setToolTipText("Regex error: " + e.getLocalizedMessage());
            }

            if (conFont != null) {
                newField.setFont(conFont);
            }
                       
            pnlDeclensions.add(newLabel);
            pnlDeclensions.add(newField);
            
            fieldMap.put(curDec.getCombinedDimId(), newField);
            labelMap.put(curDec.getCombinedDimId(), curDec.getNotes());
            
            numFields++;
        }
    }

    /**
     * sets basic properties of form based on contents
     */
    private void setFormProps() {
        double maxHeight = Toolkit.getDefaultToolkit().getScreenSize().getHeight()/2;
        int setHeight;
        
        // firstField only set if no objects initialized: do nothing
        if (firstField == null){
            return;
        }   
        
        textHeight = firstField.getMinimumSize().height;
        
        // the max height determines whether the window would be too big to fit onto the screen
        if (maxHeight <= numFields * textHeight) {
            setHeight = (int)maxHeight;
        } else {
            setHeight = numFields * textHeight;
        }        
        
        pnlDeclensions.setSize(pnlDeclensions.getSize().width, numFields * textHeight + 10);
        pnlDeclensions.setLayout(new GridLayout(0, 2));
        this.setSize(this.getWidth() + 10, pnlDeclensions.getHeight() + 70);
        
        scrDeclensions.setSize(pnlDeclensions.getSize().width, setHeight);
    }

    /**
     * Populates object with all currently existing declensions for word
     * (including deprecated ones)
     */
    private void getAllWordDeclensions() {
        Iterator<DeclensionNode> decIt = core.getDeclensionManager().getDeclensionListWord(word.getId()).iterator();

        while (decIt.hasNext()) {
            DeclensionNode curNode = decIt.next();
            allWordDeclensions.put(curNode.getCombinedDimId(), curNode);
        }
    }

    /**
     * builds all aspects of the form which are generated per case
     */
    private void buildForm() {
        // creates list of all declensions in word (even deprecated ones
        allWordDeclensions = core.getDeclensionManager().getWordDeclensions(word.getId());

        chkAutogenOverride.setSelected(word.isOverrideAutoDeclen());
        
        createFields();

        createDeprecatedFields();
        
        setFormProps();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnOk;
    private javax.swing.JCheckBox chkAutogenOverride;
    private javax.swing.JPanel pnlDeclensions;
    private javax.swing.JScrollPane scrDeclensions;
    // End of variables declaration//GEN-END:variables
}
