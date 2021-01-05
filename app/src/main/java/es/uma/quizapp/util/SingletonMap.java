package es.uma.quizapp.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Clase para implementar el patron Singleton con un Map
 * Metodos disponibles: get, put y getOrDefault
 * Constructor privado
 * Se usa el metodo de clase getInstance() para obtener la unica instancia
 */
public class SingletonMap {

    private Map<String,Object> singletonMap = new HashMap<>();;
    private static SingletonMap instance;

    private SingletonMap() {}

    public static SingletonMap getInstance() {
        if(instance == null) {
            instance = new SingletonMap();
        }
        return instance;
    }

    public Object put(String key, Object object){
        return singletonMap.put(key, object);
    }

    public Object get(String key){
        return singletonMap.get(key);
    }

    public Object getOrDefault(String key, Object object) {
        return singletonMap.getOrDefault(key, object);
    }


}
