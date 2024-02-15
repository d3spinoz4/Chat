
const express = require("express");
const bodyParser = require('body-parser');
const fileUpload = require('express-fileupload');
const cors = require('cors');
const path = require("path");
var connection  = require('express-myconnection'); 
var  mariadb = require('mariadb');

const app = express();
app.use(bodyParser.json());
const port = process.env.PORT || 80;

// Serve any static files built by React
app.use(express.static(path.join(__dirname, "webrtc-app/dist")));
//app.use(express.static('upload')); //to access the files in updload folder
app.use(cors()); // it enables all cors requests
app.use(fileUpload());

app.use(function(req, res, next) {
  res.header("Access-Control-Allow-Origin", "*");
  res.header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
  next();
});

app.get("/", function(req, res) {

  res.sendFile(path.join(__dirname, "webrtc-app/dist", "index.html"));

});

app.listen(port, () => console.log(`Listening on port ${port}`));

