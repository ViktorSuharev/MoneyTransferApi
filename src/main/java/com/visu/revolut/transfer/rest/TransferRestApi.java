package com.visu.revolut.transfer.rest;

import com.visu.revolut.transfer.api.Transfer;
import com.visu.revolut.transfer.datamodel.TransferDetail;
import com.visu.revolut.transfer.utils.OperationResponse;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/revolut")
public class TransferRestApi {

    @POST
    @Path("/transfer")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response transfer(TransferDetail transferDetail) {
        Transfer transfer = new Transfer(transferDetail);
        OperationResponse result = transfer.transfer();
        return Response.ok(result).build();
    }
}
