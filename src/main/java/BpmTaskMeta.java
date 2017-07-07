import java.util.List;

/**
 * Created by VBeliaev on 07.07.2017.
 */
public class BpmTaskMeta {

    public List<Data> data;
    public boolean editable;
    public String field;
    public String label;
    public int propType;
    public int requestId;
    public String type;
    public Validation[] validation;
    public Value value;
    public String asyncFunc;

}

class Data{
    public String id;
    public String value;
}

class Validation{
    public String name;
    public String message;
}

class Value{
    public String id;
    public String value;

    public Value(String value) {
        this.value = value;
    }
}