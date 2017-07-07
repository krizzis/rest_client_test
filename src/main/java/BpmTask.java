public class BpmTask {

    public Assignee assignee;
    public int id;
    public boolean interactive;
    public String name;
    public int requestId;
    public Sla sla;
    public String startDate;
    public int status;
}

class Assignee{
    String id;
    String name;
}

class Sla{
    public int currentLevel;
    public String dueDate;
}

