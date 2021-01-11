package com.cosmoneural.data.mongottl;

import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
public class DemoController {

    @Resource
    private OfferRepository offerRepository;


    @GetMapping("/offers")
    public List<Offer> getOffers(){
        return StreamSupport.stream(offerRepository.findAll().spliterator(), false).collect(Collectors.toList());
    }

    @GetMapping("/offer")
    public Offer getOffersbyId(@RequestParam String id){
        return offerRepository.findById(id).get();
    }

    @PutMapping("/offer")
    public Offer createOffer(@RequestBody Offer offer)
    {
        return offerRepository.save(offer);
    }

    @GetMapping("/bulkcreate")
    public String bulkcreate(){
        Offer offer = new Offer(
                "ABH1", "ABH1", Offer.OfferType.COMPLEX, 1, new Date());





        offerRepository.saveAll(Arrays.asList(new Offer(
                        "ABH2", "ABH1", Offer.OfferType.COMPLEX, 1, new Date())
                ,new Offer(
                        "ABH3", "ABH1", Offer.OfferType.COMPLEX, 1, new Date())
                , new Offer(
                        "ABH4", "ABH1", Offer.OfferType.COMPLEX, 1, new Date())
                , new Offer(
                        "ABH5", "ABH1", Offer.OfferType.COMPLEX, 1, new Date())));

        offerRepository.save(offer);

        return "Offers are created";
    }

}
