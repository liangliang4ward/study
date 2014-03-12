package UI.Component;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractCellEditor;
import javax.swing.DefaultCellEditor;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.UIManager;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import database.util.DBfactory;

public class   ButtonColumn   extends   AbstractCellEditor   implements   TableCellRenderer,   TableCellEditor   { 
    JButton   renderButton; 
    JButton   editButton; 
    String   text; 
  
  public  ButtonColumn()   { 
        renderButton   =   new   JButton(); 
        editButton   =   new   JButton(); 
        editButton.setFocusPainted(false); 
    } 

    public   Component   getTableCellRendererComponent(JTable   table, 
            Object   value,   boolean   isSelected,   boolean   hasFocus,   int   row, 
            int   column)   { 
        if   (hasFocus)   { 
            renderButton.setForeground(table.getForeground()); 
            renderButton.setBackground(UIManager 
                    .getColor( "Button.background ")); 
        }   else   if   (isSelected)   { 
            renderButton.setForeground(table.getSelectionForeground()); 
            renderButton.setBackground(table.getSelectionBackground()); 
        }   else   { 
            renderButton.setForeground(table.getForeground()); 
            renderButton.setBackground(UIManager 
                    .getColor( "Button.background ")); 
        } 

        renderButton.setText((value   ==   null)   ?    " "   :   "É¾³ý"); 
        return   renderButton; 
    } 

    public   Component   getTableCellEditorComponent(JTable   table, 
            Object   value,   boolean   isSelected,   int   row,   int   column)   { 
    	
        text   =   (value   ==   null)   ?    " "   :   value.toString(); 
        if(value!=null){
        	DBfactory.getDBfactory().delSampleandvalue(text);
        	
        	
        }
        editButton.setText("É¾³ý"); 
        return   editButton; 
    } 

    public   Object   getCellEditorValue()   { 
        return   text; 
    } 
} 

