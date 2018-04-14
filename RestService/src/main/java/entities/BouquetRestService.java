package entities;


import com.flowergarden.bouquet.MarriedBouquet;
import com.flowergarden.dao.BouquetDAO;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/bouquetService")
public class BouquetRestService {
    private BouquetDAO dao;

    public BouquetRestService(){
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.sqlite.JDBC");
        dataSource.setUrl("jdbc:sqlite:D:/Documents/Java-Advanced-Feb-Apr-2018/FlowerGarden/flowergarden.db");
        dao = new BouquetDAO(dataSource, "main","married_bouquet");
    }

    @GET
    @Path("/bouquet/{id}")
    @Produces({MediaType.APPLICATION_XML})
    public MarriedBouquet getMessage(@PathParam("id") int id) {
        return dao.getByKey(id);
    }


}