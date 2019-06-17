package dctb.beans;

import oracle.jbo.DMLException;
import oracle.jbo.Row;
import oracle.jbo.RowSetIterator;

public class Dml {
    ToolBarUtil mybean = new ToolBarUtil();
    private String MethodNameorIteratorBinding = "";
    private String InsertType = "";

    public String CreateInsert() {
        if (getMethodNameorIteratorBinding() != null) {
            RowSetIterator Schedual =
                mybean.AccessIteratorBinding(getMethodNameorIteratorBinding()).getRowSetIterator();
            Row lastRow, newRow = Schedual.createRow();
            if (getInsertType().equalsIgnoreCase("last")) {
                lastRow = Schedual.last();
            } else {
                lastRow = Schedual.getCurrentRow();
            }
            int lastRowIndex = Schedual.getRangeIndexOf(lastRow);
            newRow.setNewRowState(Row.STATUS_INITIALIZED);
            Schedual.insertRowAtRangeIndex(lastRowIndex + 1, newRow);
            Schedual.setCurrentRow(newRow);

        }
        return null;
    }

    public String Delete() {
        if (getMethodNameorIteratorBinding() != null) {
            (mybean.AccessIteratorBinding(getMethodNameorIteratorBinding())).getRowSetIterator().removeCurrentRow();
        }
        return null;
    }

    public String Next() {
        if (getMethodNameorIteratorBinding() != null) {
            (mybean.AccessIteratorBinding(getMethodNameorIteratorBinding())).getRowSetIterator().next();
        }
        return null;
    }

    public String Previous() {
        if (getMethodNameorIteratorBinding() != null) {
            (mybean.AccessIteratorBinding(getMethodNameorIteratorBinding())).getRowSetIterator().previous();
        }
        return null;
    }

    public String First() {
        if (getMethodNameorIteratorBinding() != null) {
            (mybean.AccessIteratorBinding(getMethodNameorIteratorBinding())).getRowSetIterator().first();
        }
        return null;
    }

    public String Last() {
        if (getMethodNameorIteratorBinding() != null) {
            (mybean.AccessIteratorBinding(getMethodNameorIteratorBinding())).getRowSetIterator().last();
        }
        return null;
    }

    public String Commit() {
        if (getMethodNameorIteratorBinding() != null) {
            try {
                mybean.AccessIteratorBinding(getMethodNameorIteratorBinding())
                      .getDataControl()
                      .commitTransaction();
                mybean.Validate(mybean.getFacesContext(), "Commit ..", "Data has been Successfully Saved !", 4);
            } catch (DMLException e) {
                mybean.Validate(mybean.getFacesContext(), "Commit ..", e.getErrorCode() + " - " + e.getBaseMessage(),
                                1);
            }
        }
        return null;
    }

    public String Rollback() {
        if (getMethodNameorIteratorBinding() != null) {
            try {
                mybean.AccessIteratorBinding(getMethodNameorIteratorBinding())
                      .getDataControl()
                      .rollbackTransaction();
                mybean.Validate(mybean.getFacesContext(), "RollBack ..", "Data has been Reverted Back !", 4);
            } catch (DMLException e) {
                mybean.Validate(mybean.getFacesContext(), "RollBack ..", e.getErrorCode() + " - " + e.getBaseMessage(),
                                1);
            }
        }
        return null;
    }

    public String information() {
        mybean.Validate(mybean.getFacesContext(), "STB ..", "STB | Smart ToolBar has been Developed by 'Wael Abdeen'.",
                        4);
        return null;
    }

    public void setMethodNameorIteratorBinding(String MethodNameorIteratorBinding) {
        this.MethodNameorIteratorBinding = MethodNameorIteratorBinding;
    }

    public String getMethodNameorIteratorBinding() {
        return MethodNameorIteratorBinding;
    }


    public void setInsertType(String InsertType) {
        this.InsertType = InsertType;
    }

    public String getInsertType() {
        return InsertType;
    }
}

