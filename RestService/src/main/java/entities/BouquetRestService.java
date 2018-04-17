package entities;


import com.flowergarden.bouquet.MarriedBouquet;
import com.flowergarden.dao.BouquetDAO;
import com.flowergarden.exceptions.StaleFlowerException;
import com.flowergarden.service.FreshnessService;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/bouquetService")
public class BouquetRestService {
    private BouquetDAO dao;

    public BouquetRestService() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.sqlite.JDBC");
        dataSource.setUrl("jdbc:sqlite:D:/Documents/Java-Advanced-Feb-Apr-2018/FlowerGarden/flowergarden.db");
        dao = new BouquetDAO(dataSource, "main", "married_bouquet");
    }

    @GET
    @Path("/bouquet")
    @Produces({MediaType.TEXT_XML})
    public Response getAllBouquets() {
        List <MarriedBouquet> bouquets = dao.getAll();
        if (bouquets == null)
            return Response.status(404).entity("Bouquets not found").build();
        return Response.status(200).entity(bouquets).build();
    }

    @GET
    @Path("/bouquet/{id}")
    @Produces({MediaType.TEXT_XML})
    public Response getBouquetById(@PathParam("id") int id) {
        MarriedBouquet bouquet = dao.getByKey(id);
        if (bouquet == null)
            return Response.status(404).entity("Cannot get bouquet with id " + id).build();
        return Response.status(200).entity(bouquet).build();
    }

    @GET
    @Path("/bouquet/{id}/price")
    @Produces({MediaType.TEXT_XML})
    public Response getBouquetPrice(@PathParam("id") int id) {
        MarriedBouquet bouquet = dao.getByKey(id);
        if (bouquet == null)
            return Response.status(404).entity("Cannot get bouquet with id " + id).build();
        return Response.status(200).entity(bouquet.getPrice()).build();
    }

    @DELETE
    @Path("/bouquet/{id}")
    @Produces({MediaType.TEXT_HTML})
    public Response deleteBouquet(@PathParam("id") int id) {
        boolean result = dao.deleteByKey(id);
        if (result)
            return Response.status(204).entity("Bouquet is deleted").build();
        else
            return Response.status(404).entity("Cannot delete bouquet with id " + id).build();
    }

    @POST
    @Path("/bouquet")
    @Produces({MediaType.TEXT_HTML})
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response createBouquet(@FormParam("assemblePrice") float price) {
        MarriedBouquet bouquet = new MarriedBouquet(price);
        boolean result = dao.create(bouquet);
        if (result)
            return Response.status(200).entity("Bouquet is created").build();
        else
            return Response.status(404).entity("Cannot create bouquet").build();
    }

    @PUT
    @Path("/bouquet")
    @Produces({MediaType.TEXT_HTML})
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response updateBouquet(@FormParam("id") int id, @FormParam("assemblePrice") float price) {
        MarriedBouquet bouquet = new MarriedBouquet(id, price);
        boolean result = dao.update(bouquet);
        if (result)
            return Response.status(200).entity("Bouquet is updated").build();
        else
            return Response.status(404).entity("Cannot update bouquet").build();
    }

    @PUT
    @Path("/freshness/{bouquetId}")
    @Produces({MediaType.TEXT_HTML})
    public Response updateFreshness(@PathParam("bouquetId") int id){
        MarriedBouquet bouquet = dao.getByKey(id);
        try {
            FreshnessService.decreaseFreshness(bouquet);
        } catch (StaleFlowerException e) {
            return Response.status(406).entity("Some flowers are stale").build();
        }
        return Response.status(204).entity("Freshness is updated").build();
    }
}