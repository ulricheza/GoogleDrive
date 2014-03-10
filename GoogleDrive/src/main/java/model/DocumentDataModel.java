/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package model;

import entity.Document;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.faces.model.ListDataModel;
import org.primefaces.model.SelectableDataModel;

public class DocumentDataModel extends ListDataModel<Document> 
                               implements SelectableDataModel<Document>, Serializable{

    private final Map<Long,Document> data = new HashMap();
    
    public DocumentDataModel () {}
    
    public DocumentDataModel (List<Document> documents){
        super(documents);
        for (Document doc : documents)
            data.put(doc.getId(), doc);
    }
    
    @Override
    public Object getRowKey(Document document) {
        return document.getId();
    }

    @Override
    public Document getRowData(String rowKey) {
        return data.get(Long.parseLong(rowKey));        
    }
}