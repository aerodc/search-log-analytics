package controllers;

import jsons.output.CountJson;
import jsons.output.QueryCountListJson;
import play.Logger;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;
import services.QueryService;
import utils.DateTimeUtils;

import javax.inject.Inject;
import java.io.IOException;
import java.net.URLDecoder;

public class Queries extends Controller {

    private final QueryService queryService;

    @Inject
    public Queries(QueryService queryService) {
        this.queryService = queryService;
    }

    public Result count(String date) {

        try {
            String cleanDate = URLDecoder.decode(date.trim(), "UTF-8");
            if (!DateTimeUtils.checkInputDate(cleanDate)) {
                return Results.badRequest("invalid date format");
            }

            CountJson countJson = new CountJson(this.queryService.getCount(cleanDate));
            return ok(Json.toJson(countJson));
        } catch (IOException e) {
            Logger.error(e.getMessage());
            return Results.internalServerError("internal errors");
        }
    }

    public Result popular(String date, int size) {

        try {
            String cleanDate = URLDecoder.decode(date.trim(), "UTF-8");
            if (!DateTimeUtils.checkInputDate(cleanDate)) {
                return Results.badRequest("invalid date format");
            }
            QueryCountListJson queryCountListJson = new QueryCountListJson(this.queryService.getQueries(cleanDate, size));
            return ok(Json.toJson(queryCountListJson));
        } catch (IOException e) {
            Logger.error(e.getMessage());
            return Results.internalServerError("internal errors");
        }
    }
}
