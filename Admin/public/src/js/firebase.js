 
import firebase from 'firebase';

let Firebase = firebase;

const config = {
  apiKey: "AIzaSyBPcP8uM26qoZVlwX_8t2CPnY_4wcs7B24",
  authDomain: "money-transfer-279c0.firebaseapp.com",
  databaseURL: "https://money-transfer-279c0.firebaseio.com",
  projectId: "money-transfer-279c0",
  storageBucket: "money-transfer-279c0.appspot.com",
  messagingSenderId: "644155681486",
  appId: "1:644155681486:web:c579f3163514760eb2be9f",
  measurementId: "G-QN01VMFPRZ"
};

Firebase.initializeApp(config);

export default Firebase;

