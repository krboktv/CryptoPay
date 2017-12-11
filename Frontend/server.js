var express = require('express');
var http = require('http');
var path = require('path');
const fs = require("fs");
var bodyParser = require("body-parser");
var hbs = require("hbs");
const WavesAPI = require('waves-api');
var ip = require('ip');
var MongoClient = require("mongodb").MongoClient;
var ObjectID = require("mongodb").ObjectID;
var URL = require('url-parse');
var request = require('sync-request');

//Получение баланса Waves
const Waves = WavesAPI.create(WavesAPI.TESTNET_CONFIG);

// console.log(g);

// setTimeout(function() {console.log(g);}, 1000, g); 
function convertFromHex(hex) {
    var hex = hex.toString();//force conversion
    var str = '';
    for (var i = 0; i < hex.length; i += 2)
        str += String.fromCharCode(parseInt(hex.substr(i, 2), 16));
    return str;
}

function convertToHex(str) {
    var hex = '';
    for(var i=0;i<str.length;i++) {
        hex += ''+str.charCodeAt(i).toString(16);
    }
    return hex;
}



// создаем парсер для данных application/x-www-form-urlencoded
var app = express();
var db;
var urlencodedParser = bodyParser.urlencoded({extended: false});

app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: true}));

app.use(express.static(path.join(__dirname, 'views/public')));


    // Регистрация
    app.post('/kek', function (req, res) {
        var users = {
            email: req.body.email,
            address: req.body.address
        };        

        var txt = users.email + '.txt';
        
        var hexData = convertToHex(users.address);

        fs.writeFile(txt, hexData, function(error){

            if(error) throw error; // если возникла ошибка
            var data = fs.readFileSync(txt, "utf8");
      
        });
        // db.collection('users').insert(users, function (err, result) {
        //     if(err) {
        //         console.log(err);
        //         return res.sendStatus(500);
        //     }
        //     res.send(users);
        // });
        res.send("success");
    });

    // Логин
    app.get("/auth/:email", function(req, res) {
        var j = URL(req.url, true);
        var gg = j.query.pathname+'.txt';
        var fileContent = fs.readFileSync(gg, "utf8");
        var convertedData = convertFromHex(fileContent);
        res.send(convertedData);

        // fs.writeFile(txt, users.address, function(error){
            
        //                 if(error) throw error; // если возникла ошибка
        //                 var data = fs.readFileSync(txt, "utf8");
                  
        //             });


        // db.collection('users').findOne({email: req}, function (err, docs) {
        //     if(err) {
        //         console.log(err);
        //         return res.sendStatus(500);
        //     }
        //     res.send(docs);
        // });
    });
    // app.post("/auth", function(req, res) {
    //     console.log(url.query.email);
    //     // db.collection('users').find().toArray(function (err, docs) {
    //     //     if(err) {
    //     //         console.log(err);
    //     //         return res.sendStatus(500);
    //     //     }
    //     //     res.send(docs);
    //     // });
    // });
   

    // function balanceOf(addr) {
    //     var g = Waves.API.Node.v1.addresses.balanceDetails(addr).then((balanceDetails) => {
    //     var a = balanceDetails.available;  
    //     console.log("Первый баланс типа "+typeof a+" равен "+a);
    
    //     setTimeout(function() {
    //         var balanceAfter = Waves.API.Node.v1.addresses.balanceDetails(addr).then((balanceDetails) => {
    //             var b = balanceDetails.available;
    //             console.log("Второй баланс типа "+typeof b+" равен "+b);
    //             if(b>a) {
    //                 console.log('true');
    //                 return 'true';
    //             }
    //             else {
    //                 console.log('false');
    //                 return 'false';
    //             }
    //             });         
    //         }
    //         , 5000);
    //     });
    // }

    app.get("/lol/:email", function(req, res) {
        // res.render("public/index.hbs");
        var j = URL(req.url, true);
        var gg = j.pathname.substr(11);
        var txt = j.query.pathname + '.txt';
        var txt1 = j.query.pathname + '1.txt';
        var fileContent = fs.readFileSync(txt, "utf8");
        var convertedData = convertFromHex(fileContent);
        var g = Waves.API.Node.v1.addresses.balanceDetails(convertedData).then((balanceDetails) => {
        var a = balanceDetails.available;  
        fs.writeFile(txt1, a, function(error){
                    res.sendfile(txt1);
                    // if(error) throw error; // если возникла ошибка
                    // var read = fs.readFileSync(txt1, "utf8");
                    // res.send(read);
                });
       
        // if(fs.readFileSync(txt1, "utf8")) {
        //     var read = fs.readFileSync(txt1, "utf8");
        //     res.send(read);
        // }
        // else {
        //     console.log("Первый баланс типа "+typeof a+" равен "+a);
        //         var k = txt + "1";
        //     fs.writeFile(k, a, function(error){
        //         if(error) throw error; // если возникла ошибка
        //     });
        // }

        // setTimeout(function() {
        //     var balanceAfter = Waves.API.Node.v1.addresses.balanceDetails(convertedData).then((balanceDetails) => {
        //         var b = balanceDetails.available;
        //         console.log("Второй баланс типа "+ Number(b) +" равен "+b);
        //         if(b>a) {
        //             console.log('true');
        //             var es = res.send('true');
        //             return es;
        //         }
        //         else {
        //             console.log('false');
        //             var no = res.send('false'); 
        //             return no;
        //         }
        //         });         
        //     }
        //     , 3000);  
                     
        });
        
    });
   
    app.get("/", function(req, res) {
        res.render("public/index.hbs");
    });

    // app.get("/check/:email", function(req, res) {
    //     var j = URL(req.url, true);
    //     var txt = j.query.email + '1.txt';
    //     var fileContent = fs.readFileSync(txt, "utf8");
    // });
    // app.get("/rega", function(request, response) {
    //     response.render("public/rega.hbs");
    // });


   app.get("/rega", function (request, response) {
    const seed = Waves.Seed.create();
    
    /* Создаём кошелёк. 
    {{getPhrase}} - сид
    {{getAddress}} - адрес
    {{getPrivateKey}} - приватный ключ
    {{getPublicKey}} - приватный ключ
    */
    hbs.registerHelper("getPhrase", function(){
        return seed.phrase;
    });
    hbs.registerHelper("getAddress", function(){
        return seed.address;
    });
    hbs.registerHelper("getPrivateKey", function(){
        return seed.keyPair.privateKey;
    });
    hbs.registerHelper("getPublicKey", function(){
        return seed.keyPair.publicKey;
    });

    app.set("view engine", "hbs");
    if(!request.body) return response.sendStatus(400);
    console.log(request.body);
    // response.send(`${request.body.login} - ${request.body.password}`);
    console.log(request.body.login);
    response.render("public/rega.hbs");
});

// Регистрация. БД

// определяем обработчик для маршрута "/"
// app.get("/", function(request, response){
//     console.log("Rabotaet");
//     // отправляем ответ
    
//     response.send('<h1>ПОЖАЛУЙСТА</h1>');
// });
// начинаем прослушивать подключения на 3000 порту

// MongoClient.connect('mongodb://localhost:27017/blockteamBD', function(err, database) {
//     if(err) {
//         return console.log(err);
//     }
//     db = database;
    app.listen(80);
    console.log("Rabotaet");
    console.log(ip.address());
// });
