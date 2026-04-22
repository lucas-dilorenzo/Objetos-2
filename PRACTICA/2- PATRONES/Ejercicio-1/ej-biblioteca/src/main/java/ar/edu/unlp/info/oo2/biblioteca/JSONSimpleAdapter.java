package ar.edu.unlp.info.oo2.biblioteca;

import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class JSONSimpleAdapter implements Exporter {

    @Override
    public String exportar(List<Socio> socios) {
        JSONArray array = new JSONArray();
        for (Socio socio : socios) {
            JSONObject obj = new JSONObject();
            obj.put("nombre", socio.getNombre());
            obj.put("email",  socio.getEmail());
            obj.put("legajo", socio.getLegajo());
            array.add(obj);
        }
        return array.toJSONString();
    }
}
