package dctb.beans;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import javax.faces.event.ActionEvent;

import oracle.adf.model.BindingContext;
import oracle.adf.model.binding.DCBindingContainer;
import oracle.adf.model.binding.DCIteratorBinding;

import oracle.adf.view.rich.context.AdfFacesContext;

import oracle.binding.BindingContainer;

import oracle.binding.OperationBinding;

import oracle.jbo.DMLException;
import oracle.jbo.Key;
import oracle.jbo.Row;
import oracle.jbo.RowSetIterator;

public class Dml {

    private String MethodNameorIteratorBinding = "";
    private String InsertType = "";
    private UIComponent formComponentId ;

    public BindingContainer getBindings() { // Access Data Control
        return BindingContext.getCurrent().getCurrentBindingsEntry();
    }

    private DCIteratorBinding getIteratorBinding(String IteratorName) {
        return (DCIteratorBinding) getBindings().get(IteratorName);
    }

    public FacesContext getFacesContext() { // Access Faces Context - JSF Resources
        return FacesContext.getCurrentInstance();
    }

    public void Validate(FacesContext MyContext, String Header, String Footer, int Level) { // Force Validation
        FacesMessage MyMessage = new FacesMessage();
        if (Level == 1) {
            MyMessage = new FacesMessage(MyMessage.SEVERITY_FATAL, Header, Footer);
        } else if (Level == 2) {
            MyMessage = new FacesMessage(MyMessage.SEVERITY_ERROR, Header, Footer);
        } else if (Level == 3) {
            MyMessage = new FacesMessage(MyMessage.SEVERITY_WARN, Header, Footer);
        } else if (Level == 4) {
            MyMessage = new FacesMessage(MyMessage.SEVERITY_INFO, Header, Footer);
        }
        MyContext.addMessage(null, MyMessage);
    }

    public String CreateInsert() {
        if (getMethodNameorIteratorBinding() != null) {
            RowSetIterator Schedual = getIteratorBinding(getMethodNameorIteratorBinding()).getRowSetIterator();
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
            getIteratorBinding(getMethodNameorIteratorBinding()).getRowSetIterator().removeCurrentRow();
            AdfFacesContext adfFacesContext = AdfFacesContext.getCurrentInstance();
            adfFacesContext.addPartialTarget(formComponentId); // pass java binding of  ui component
        }
        return null;
    }

    public String Delete2() {
        BindingContainer bindings = getIteratorBinding(getMethodNameorIteratorBinding()).getBindingContainer();
        OperationBinding operationBinding = bindings.getOperationBinding("Delete");
        operationBinding.execute();
        if (!operationBinding.getErrors().isEmpty()) {
            return null;
        }
        return null;
    }

    public String Next() {
        if (getMethodNameorIteratorBinding() != null) {
            getIteratorBinding(getMethodNameorIteratorBinding()).getRowSetIterator().next();
        }
        return null;
    }

    public String Previous() {
        if (getMethodNameorIteratorBinding() != null) {
            getIteratorBinding(getMethodNameorIteratorBinding()).getRowSetIterator().previous();
        }
        return null;
    }

    public String First() {
        if (getMethodNameorIteratorBinding() != null) {
            getIteratorBinding(getMethodNameorIteratorBinding()).getRowSetIterator().first();
        }
        return null;
    }

    public String Last() {
        if (getMethodNameorIteratorBinding() != null) {
            getIteratorBinding(getMethodNameorIteratorBinding()).getRowSetIterator().last();
        }
        return null;
    }

    public String Commit() {
        if (getMethodNameorIteratorBinding() != null) {
            try {
                getIteratorBinding(getMethodNameorIteratorBinding()).getDataControl().commitTransaction();
                Validate(getFacesContext(), "Commit ..", "Data has been Successfully Saved !", 4);
            } catch (DMLException e) {
                Validate(getFacesContext(), "Commit ..", e.getErrorCode() + " - " + e.getBaseMessage(), 1);
            }
        }
        return null;
    }

    public void undo_action() {
        getIteratorBinding(getMethodNameorIteratorBinding()).getCurrentRow()
            .refresh(Row.REFRESH_FORGET_NEW_ROWS | Row.REFRESH_WITH_DB_FORGET_CHANGES | Row.REFRESH_UNDO_CHANGES |
                     Row.STATUS_NEW | Row.STATUS_INITIALIZED);

        //notify consumer about event
        //   invokeActionListenerExpression(actionEvent);

    }

    public String Rollback() {
        //BindingContext context = BindingContext.getCurrent();
        //DCBindingContainer dcBindCont = (DCBindingContainer) context.getCurrentBindingsEntry();

        if (getMethodNameorIteratorBinding() != null) {
            System.out.println("Inside Roll Back");
            //DCIteratorBinding bindings = getIteratorBinding(getMethodNameorIteratorBinding());
            //Get current row key
            // Key parentKey = bindings.getCurrentRow().getKey();
            //You can add your operation code here, I have used simple Cancel operation with Rollback and Execute
            System.out.println("Before Try Catch");
            try {
                System.out.println("Row Status: " + Row.STATUS_NEW + "Row Initialed" + Row.STATUS_INITIALIZED);
                //dcBindCont.setExecuteOnRollback(false);
                getIteratorBinding(getMethodNameorIteratorBinding()).getDataControl().rollbackTransaction();
                getIteratorBinding(getMethodNameorIteratorBinding()).getCurrentRow()
                    .refresh(Row.STATUS_NEW | Row.STATUS_INITIALIZED);
                System.out.println("Inside Try Catch Code");
                //Set again row key as current row
                //parentIter.setCurrentRowWithKey(parentKey.toStringFormat(true));
                Validate(getFacesContext(), "RollBack ..", "Data has been Reverted Back !", 4);
            } catch (DMLException e) {
                Validate(getFacesContext(), "RollBack ..", e.getErrorCode() + " - " + e.getBaseMessage(), 1);
            }
        }
        return null;
    }

    public String information() {
        Validate(getFacesContext(), "STB ..", "STB | Smart ToolBar has been Developed by 'Wael Abdeen'.", 4);
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

