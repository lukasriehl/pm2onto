package com.lukas.pm2onto.consulta;

import com.lukas.pm2onto.model.Ontologia;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 *
 * @author lukas
 */
public class LazyOntologiaDataModel extends LazyDataModel<Ontologia> {

    private List<Ontologia> dsOntologia;

    public LazyOntologiaDataModel(List<Ontologia> dsOntologia) {
        this.dsOntologia = dsOntologia;
    }

    @Override
    public Ontologia getRowData(String rowKey) {
        for (Ontologia ont : dsOntologia) {
            if (ont.getIdOntologia().toString().equals(rowKey)) {
                return ont;
            }
        }

        return null;
    }

    @Override
    public Object getRowKey(Ontologia ontologia) {
        return ontologia.getIdOntologia();
    }

    @Override
    public List<Ontologia> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        List<Ontologia> data = new ArrayList();

        //filter
        for (Ontologia ontologia : dsOntologia) {
            boolean match = true;

            if (filters != null) {
                for (Iterator<String> it = filters.keySet().iterator(); it.hasNext();) {
                    try {
                        String filterProperty = it.next();
                        Object filterValue = filters.get(filterProperty);
                        Field field1 = Ontologia.class.getDeclaredField(filterProperty);
                        field1.setAccessible(true);
                        String fieldValue = String.valueOf(field1.get(ontologia));

                        if (filterValue == null || fieldValue.startsWith(filterValue.toString())) {
                            match = true;
                        } else {
                            match = false;
                            break;
                        }
                    } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
                        match = false;
                    }
                }
            }

            if (match) {
                data.add(ontologia);
            }
        }

        //sort
        if (sortField != null) {
            Collections.sort(data, new OntologiaLazySorter(sortField, sortOrder));
        }

        //rowCount
        int dataSize = data.size();
        this.setRowCount(dataSize);

        //paginate
        if (dataSize > pageSize) {
            try {
                return data.subList(first, first + pageSize);
            } catch (IndexOutOfBoundsException e) {
                return data.subList(first, first + (dataSize % pageSize));
            }
        } else {
            return data;
        }
    }
}
