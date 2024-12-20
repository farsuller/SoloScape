package com.soloscape.note.model

import com.soloscape.ui.R

data class Location(
    val image: Int,
    val title: String,
    val subtitle: String,
    val rating: Int = 0,
)

val locations = listOf(
    Location(
        image = R.drawable.tabriz,
        title = "Tabriz",
        subtitle = "Tabriz is the capital city of East Azerbaijan Province, in northwestern Iran. Tabriz Bazaar, once a major Silk Road market, is a sprawling brick-vaulted complex selling carpets, spices and jewelry\"",
        rating = 5,
    ),
    Location(
        image = R.drawable.dubai,
        subtitle = "Dubai is a city and emirate in the United Arab Emirates known for luxury shopping, ultramodern architecture and a lively nightlife scene. Burj Khalifa, an 830m-tall tower, dominates the skyscraper-filled skyline.",
        rating = 3,
        title = "Dubai",
    ),
    Location(
        image = R.drawable.los_angeles,
        title = "Los Angeles",
        rating = 4,
        subtitle = "Los Angeles is a sprawling Southern California city and the center of the nation’s film and television industry. Near its iconic Hollywood sign, studios such as Paramount Pictures, Universal and Warner Brothers offer behind-the-scenes tours.",
    ),
    Location(
        image = R.drawable.london,
        title = "London",
        rating = 3,
        subtitle = "London, the capital of England and the United Kingdom, is a 21st-century city with history stretching back to Roman times.",
    ),
    Location(
        image = R.drawable.sweden,
        title = "Sweden",
        rating = 5,
        subtitle = "Sweden is a Scandinavian nation with thousands of coastal islands and inland lakes, along with vast boreal forests and glaciated mountains.",
    ),
    Location(
        image = R.drawable.kazan,
        title = "Kazan",
        rating = 2,
        subtitle = "Kazan is a city in southwest Russia, on the banks of the Volga and Kazanka rivers. The capital of the Republic of Tatarstan",
    ),
)
