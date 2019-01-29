package com.lukas.pm2onto.consulta;

import com.lukas.pm2onto.model.Ontologia;
import java.lang.reflect.Field;
import java.util.Comparator;
import org.primefaces.model.SortOrder;

/**
 *
 * @author lukas
 */
public class OntologiaLazySorter implements Comparator<Ontologia>{

    private String sortField;
    
    private SortOrder sortOrder;
    
    public OntologiaLazySorter(String sortField, SortOrder sortOrder){
        this.sortField = sortField;
        this.sortOrder = sortOrder;
    }
    
    @Override
    public int compare(Ontologia o1, Ontologia o2) {
        try {
            Field field1 = Ontologia.class.getDeclaredField(this.sortField);
            field1.setAccessible(true);
            Object value1 = field1.get(o1);
            Object value2 = field1.get(o2);
 
            int value = ((Comparable)value1).compareTo(value2);
             
            return SortOrder.ASCENDING.equals(sortOrder) ? value : -1 * value;
        }
        catch(NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
            throw new RuntimeException();
        }
    }
    
}
