/*
* --------------------------------------------------------------------
*
* Package:         client
* Module:          src/services
* File:            Api.js
*
* --------------------------------------------------------------------
*/

//imports
import axios from "axios";

//Server setup
const SERVER_URL = 'http://localhost:8080/';

const Api = {

    //Product API

    getAllProduct: () => {
        return new Promise((resolve, reject) => {
            axios.get(SERVER_URL + `products/`)
                .then((res) => resolve(res.data))
                .catch((err) => {
                    console.log('ERROR:   '+ err );reject(err)});
        })
    },
    getProduct: (productId) => {
        return new Promise((resolve, reject) => {
            axios.get(SERVER_URL + `products/${productId}`)
                .then((res) => resolve(res.data))
                .catch((err) =>reject(err));
        })
    },
    //PROFILE API

    getProfileByEmail: (email) => {
        return new Promise((resolve, reject) => {
            axios.get(SERVER_URL + `profiles/${email}`)
                .then((res) => resolve(res.data))
                .catch((err) => reject(err));
        })
    },
    addNewProfile: (formData) => {
        return new Promise((resolve, reject) => {
            axios.post(SERVER_URL + 'profiles', formData)
                .then((res) => resolve(res.data))
                .catch((err) => reject(err.response.data));
        })
    },
    modifyProfile: (email, formData) => {
        return new Promise((resolve, reject) => {
            axios.put(SERVER_URL + `profiles/${email}`, formData)
                .then((res) => resolve(res.data))
                .catch((err) => reject(err));
        })
    }
}

export default Api
