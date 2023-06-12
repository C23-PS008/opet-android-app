package com.c23ps008.opet.data

object PetBreedData {
    private val cat = listOf(
        "Other",
        "Abyssinian", "Aegean", "American Bobtail", "American Shorthair",
        "American Wirehair", "Aphrodite Giant", "Arabian Mau", "Asian",
        "Australian Mist", "Bambino", "Bengal Cats", "Birman", "Bombay",
        "Brazilian Shorthair", "British Longhair", "British Shorthair", "Burmese",
        "Burmilla", "California Spangled", "Chantilly-Tiffany", "Chausie",
        "Colorpoint Shorthair", "Cornish Rex", "Cyprus", "Devon Rex", "Donskoy",
        "European Shorthair", "Foldex", "German Rex", "Highlander",
        "Japanese Bobtail", "Javanese", "Khao Manee", "Kurilian Bobtail", "Lykoi",
        "Maine Coon", "Manx", "Mekong Bobtail", "Nebelung", "Oriental Bicolor",
        "Persian", "Peterbald", "Pixie-Bob", "Ragdoll Cats", "Russian Blue",
        "Savannah", "Scottish Fold", "Serengeti", "Siamese Cat", "Siberian",
        "Singapura", "Snowshoe", "Sokoke", "Somali", "Sphynx", "American Shorthair",
        "Tonkinese", "Toyger", "Turkish Angora", "Turkish Van", "Ukrainian Levkoy",
        "York Chocolate"
    )

    private val dog = listOf(
        "Other",
        "Affenpinscher", "Afghan Hound", "Airedale Terrier", "Akita",
        "Alaskan Malamute", "American English Coonhound",
        "American Eskimo Dog", "American Foxhound",
        "American Staffordshire Terrier", "American Water Spaniel",
        "Anatolian Shepherd Dog", "Australian Cattle Dog",
        "Australian Shepherd", "Australian Terrier", "Azawakh", "Barbet",
        "Basenji", "Basset Hound", "Beagle", "Bearded Collie", "Beauceron",
        "Belgian Malinois", "Belgian Sheepdog", "Belgian Tervuren",
        "Bergamasco Sheepdog", "Berger Picard", "Bernese Mountain Dog",
        "Bichon Frise", "Black and Tan Coonhound", "Black Russian Terrier",
        "Bloodhound", "Bluetick Coonhound", "Boerboel", "Border Collie",
        "Border Terrier", "Borzoi", "Boston Terrier",
        "Bouvier des Flandres", "Boxer", "Boykin Spaniel", "Briard",
        "Brittany", "Brussels Griffon", "Bull Terrier", "Bulldog",
        "Bullmastiff", "Cairn Terrier", "Canaan Dog", "Cane Corso",
        "Cardigan Welsh Corgi", "Cavalier King Charles Spaniel",
        "Cesky Terrier", "Chesapeake Bay Retriever", "Chihuahua",
        "Chinese Crested", "Chinese Shar-Pei", "Chinook", "Chow Chow",
        "Cirneco dell’Etna", "Clumber Spaniel", "Cocker Spaniel", "Collie",
        "Coton de Tulear", "Curly-Coated Retriever", "Dachshund",
        "Dalmatian", "Dandie Dinmont Terrier", "Doberman Pinscher",
        "Dogue de Bordeaux", "English Cocker Spaniel", "English Foxhound",
        "English Setter", "English Springer Spaniel",
        "English Toy Spaniel", "Entlebucher Mountain Dog", "Field Spaniel",
        "Finnish Lapphund", "Finnish Spitz", "Flat-Coated Retriever",
        "French Bulldog", "German Pinscher", "German Shepherd Dog",
        "German Shorthaired Pointer", "German Wirehaired Pointer",
        "Glen of Imaal Terrier", "Golden Retriever", "Gordon Setter",
        "Grand Basset Griffon Vendéen", "Great Dane", "Great Pyrenees",
        "Greater Swiss Mountain Dog", "Greyhound", "Harrier", "Havanese",
        "Ibizan Hound", "Icelandic Sheepdog", "Irish Setter",
        "Irish Water Spaniel", "Irish Wolfhound", "Italian Greyhound",
        "Japanese Chin", "Keeshond", "Komondor", "Kuvasz",
        "Labrador Retriever", "Lagotto Romagnolo", "Lakeland Terrier",
        "Leonberger", "Lhasa Apso", "Löwchen", "Maltese",
        "Manchester Terrier (Standard)", "Manchester Terrier (Toy)",
        "Mastiff", "Miniature American Shepherd", "Miniature Bull Terrier",
        "Miniature Pinscher", "Miniature Schnauzer", "Neapolitan Mastiff",
        "Nederlandse Kooikerhondje", "Newfoundland", "Norfolk Terrier",
        "Norwegian Buhund", "Norwegian Elkhound", "Norwegian Lundehund",
        "Norwich Terrier", "Nova Scotia Duck Tolling Retriever",
        "Old English Sheepdog", "Otterhound", "Papillon",
        "Parson Russell Terrier", "Pekingese", "Pembroke Welsh Corgi",
        "Petit Basset Griffon Vendéen", "Pharaoh Hound", "Plott Hound",
        "Pointer", "Polish Lowland Sheepdog", "Pomeranian",
        "Poodle (Standard)", "Poodle (Toy)", "Portuguese Podengo Pequeno",
        "Portuguese Water Dog", "Pug", "Puli", "Pumi", "Pyrenean Shepherd",
        "Rat Terrier", "Redbone Coonhound", "Rhodesian Ridgeback",
        "Rottweiler", "Russell Terrier", "Saint Bernard", "Saluki",
        "Samoyed", "Schipperke", "Scottish Deerhound", "Scottish Terrier",
        "Sealyham Terrier", "Shetland Sheepdog", "Shiba Inu", "Shih Tzu",
        "Siberian Husky", "Silky Terrier", "Skye Terrier", "Sloughi",
        "Smooth Fox Terrier", "Soft Coated Wheaten Terrier",
        "Spanish Water Dog", "Staffordshire Bull Terrier",
        "Standard Schnauzer", "Sussex Spaniel", "Swedish Vallhund",
        "Tibetan Mastiff", "Tibetan Spaniel", "Tibetan Terrier",
        "Toy Fox Terrier", "Treeing Walker Coonhound", "Vizsla",
        "Weimaraner", "Welsh Terrier", "West Highland White Terrier",
        "Whippet", "Wire Fox Terrier", "Wirehaired Pointing Griffon",
        "Wirehaired Vizsla", "Xoloitzcuintli", "Yorkshire Terrier"
    )

    fun getCatBreed(): List<String> {
        return cat
    }

    fun getDogBreed(): List<String> {
        return dog
    }
}