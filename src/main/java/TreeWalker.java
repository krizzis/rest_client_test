import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jersey.repackaged.com.google.common.collect.Lists;


public class TreeWalker
{
    public static JsonNode convertJSONToNode(String json) throws JsonProcessingException, IOException
    {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(json);

        return jsonNode;
    }

    public static void walkTree(JsonNode root)
    {
        walker(null, root);
    }

    private static void walker(String nodename, JsonNode node)
    {
        String nameToPrint = nodename != null ? nodename : "must_be_root";
        System.out.println("walker - node name: " + nameToPrint);
        if (node.isObject())
        {
            Iterator<Map.Entry<String, JsonNode>> iterator = node.fields();

            ArrayList<Map.Entry<String, JsonNode>> nodesList = Lists.newArrayList(iterator);
            System.out.println("Walk Tree - root:" + node + ", elements keys:" + nodesList);
            for (Map.Entry<String, JsonNode> nodEntry : nodesList)
            {
                String name = nodEntry.getKey();
                JsonNode newNode = nodEntry.getValue();

                // System.out.println("  entry - key: " + name + ", value:" + node);
                walker(name, newNode);
            }
        }
        else if (node.isArray())
        {
            Iterator<JsonNode> arrayItemsIterator = node.elements();
            ArrayList<JsonNode> arrayItemsList = Lists.newArrayList(arrayItemsIterator);
            for (JsonNode arrayNode : arrayItemsList)
            {
                walker("array item", arrayNode);
            }
        }
        else
        {
            if (node.isValueNode())
            {
                System.out.println("  valueNode: " + node.asText());
            }
            else
            {
                System.out.println("  node some other type");
            }
        }
    }
}