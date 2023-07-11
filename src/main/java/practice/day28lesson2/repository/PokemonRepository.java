package practice.day28lesson2.repository;

import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.data.mongodb.core.aggregation.UnwindOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public class PokemonRepository {
    
    @Autowired
    private MongoTemplate mongoTemplate;


    /*
     * db.pokemon.aggregate([
        { $unwind: '$type' },
        {     $group: {
                _id: '$type'}},
        { $sort: {_id : 1}}
        ])
     */
    public List<Document> getAllTypes(){
        UnwindOperation unwindOperation = Aggregation.unwind("type");
        GroupOperation groupOperation = Aggregation.group("type");
        SortOperation sortById = Aggregation.sort(Sort.by(Direction.ASC, "_id"));
        Aggregation pipeline = Aggregation.newAggregation(unwindOperation, groupOperation, sortById);
        return mongoTemplate.aggregate(pipeline, "pokemon", Document.class).getMappedResults();
    }

    /*
     * db.pokemon.find(
        { type: { $in : [{$regex:"grass", $options:'i'}]}},
        { name: 1, img: 1})
     */
    public List<Document> getPokemonByType(String type){
        Criteria criteria = Criteria.where("type").regex(type, "i");
        Query query = Query.query(criteria);
        query.fields().exclude("_id").include("id","name","img");
        return mongoTemplate.find(query, Document.class, "pokemon");
    }

    /*
     * db.pokemon.aggregate([
        { $sort: {name: 1}},
        { $unwind: '$type' },
        {
            $group: {
                _id: '$type',
                pokemon: {$push: {
                    id: '$id',
                    name: '$name', 
                    img: '$img' }}
            }
        },
        { $sort: {'pokemon.id' : 1}}
        ])
     */
    public List<Document> getPokemonByType(){
        SortOperation sortByName = Aggregation.sort(Sort.by(Direction.ASC, "name"));
        UnwindOperation unwindOperation = Aggregation.unwind("type");

        Document pokemon = new Document();
        pokemon.put("id","id");
        pokemon.put("name", "$name");
        pokemon.put("img", "$img");
        GroupOperation groupOperation = Aggregation.group("type").push(pokemon).as("pokemon");

        SortOperation sortById = Aggregation.sort(Sort.by(Direction.ASC, "pokemon.id"));

        Aggregation pipeline = Aggregation.newAggregation(sortByName, unwindOperation, groupOperation, sortById); //not sorting numbers in String format

        return mongoTemplate.aggregate(pipeline, "pokemon", Document.class).getMappedResults();
    }
}
