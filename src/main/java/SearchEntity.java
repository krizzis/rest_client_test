import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchEntity {

    @SerializedName(value="getLuxStaffEmployeeByODataQueryStringAsyncFuncResult",
            alternate={"getLuxHireOfferByODataQueryStringAsyncFuncResult"})
    public Results results;
}

class Results{

    public List<Items> Items;
    public int TotalCount;
}

class Items {
    public String id;
    public String value;
}