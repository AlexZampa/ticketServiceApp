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
import {useEffect} from "react";

//Server setup
const SERVER_URL = 'http://localhost:8080/';

const Api = {

    //Authentication API

    login: (credentials) => {
        axios.defaults.headers.common['Authorization'] = ''
        delete axios.defaults.headers.common['Authorization']
        return new Promise((resolve, reject) => {
            axios.post(SERVER_URL + 'public/login', credentials)
                .then((res) => {
                    axios.defaults.headers.common['Authorization'] =`Bearer ${res.data.token}`
                    resolve(res.data)
                })
                .catch((err) => reject(err.response.data));
        })
    },

    signup: (formData) => {
        return new Promise((resolve, reject) => {
            axios.post(SERVER_URL + `public/signup`, formData)
                .then((res) => resolve(res.data))
                .catch((err) => reject(err.response.data));
        })
    },

    logout: () => {
        return new Promise((resolve, reject) => {
            axios.delete(SERVER_URL + 'authenticated/logout')
                .then((res) => {
                    axios.defaults.headers.common['Authorization'] = ''
                    delete axios.defaults.headers.common['Authorization']
                    resolve(res.data)
                })
                .catch((err) => reject(err.response.data));
        })
    },

    //Product API

    getAllProduct: () => {
        return new Promise((resolve, reject) => {
            axios.get(SERVER_URL + `public/products`)
                .then((res) => resolve(res.data))
                .catch((err) => reject(err.response.data));
        })
    },
    getProduct: (productId) => {
        return new Promise((resolve, reject) => {
            axios.get(SERVER_URL + `public/products/${productId}`)
                .then((res) => resolve(res.data))
                .catch((err) =>reject(err.response.data));
        })
    },
    //PROFILE API

    getProfileByEmail: (email, token) => {
        axios.defaults.headers.common['Authorization'] =`Bearer ${token}`
        return new Promise((resolve, reject) => {
            axios.get(SERVER_URL + `authenticated/profiles/${email}`)
                .then((res) => resolve(res.data))
                .catch((err) => reject(err.response.data));
        })
    },

    modifyProfile: (email, formData, token) => {
        axios.defaults.headers.common['Authorization'] =`Bearer ${token}`
        return new Promise((resolve, reject) => {
            axios.put(SERVER_URL + `authenticated/profiles/${email}`, formData)
                .then((res) => resolve(res.data))
                .catch((err) => reject(err.response.data));
        })
    },

    getAllExperts: (token) => {
        axios.defaults.headers.common['Authorization'] =`Bearer ${token}`
        return new Promise((resolve, reject) => {
            axios.get(SERVER_URL + `manager/profiles/expert`)
                .then((res) => resolve(res.data))
                .catch((err) => reject(err.response.data));
        })
    },

    // TICKET API

    getTicketById: (ticketId,token) => {
        return new Promise((resolve, reject) => {
            axios.defaults.headers.common['Authorization'] =`Bearer ${token}`
            axios.get(SERVER_URL + `authenticated/tickets/${ticketId}`)
                .then((res) => resolve(res.data))
                .catch((err) => reject(err.response.data));
        })
    },

    getOpenTickets: (token) => {
        axios.defaults.headers.common['Authorization'] =`Bearer ${token}`
        return new Promise((resolve, reject) => {
            axios.get(SERVER_URL + `manager/tickets/open`)
                .then((res) => {
                    console.log(res)
                    resolve(res.data)
                })
                .catch((err) => reject(err.response.data));
        })
    },

    getTicketsByProfile: (email,token) => {
        axios.defaults.headers.common['Authorization'] =`Bearer ${token}`
        return new Promise((resolve, reject) => {
            axios.get(SERVER_URL + `client/tickets/created/${email}`)
                .then((res) => resolve(res.data))
                .catch((err) => reject(err.response.data));
        })
    },

    getAssignedTickets: (email, token) => {
        axios.defaults.headers.common['Authorization'] =`Bearer ${token}`
        return new Promise((resolve, reject) => {
            axios.get(SERVER_URL + `expert/tickets/assigned/${email}`)
                .then((res) => resolve(res.data))
                .catch((err) => reject(err.response.data));
        })
    },

    addNewTicket: (ticket, token) => {
        axios.defaults.headers.common['Authorization'] =`Bearer ${token}`
        return new Promise((resolve, reject) => {
            axios.post(SERVER_URL + 'client/tickets', ticket)
                .then((res) => resolve(res.data))
                .catch((err) => reject(err.response.data));
        })
    },

    modifyPriority: (ticketId, priority, token) => {
        axios.defaults.headers.common['Authorization'] =`Bearer ${token}`
        return new Promise((resolve, reject) => {
            axios.put(SERVER_URL + `manager/tickets/${ticketId}/priority/${priority}`)
                .then((res) => resolve(res.data))
                .catch((err) => reject(err.response.data));
        })
    },

    assignExpert: (ticketId, form, token) => {
        axios.defaults.headers.common['Authorization'] =`Bearer ${token}`
        return new Promise((resolve, reject) => {
            axios.put(SERVER_URL + `manager/tickets/${ticketId}/expert`, form)
                .then((res) => resolve(res.data))
                .catch((err) => reject(err.response.data));
        })
    },

    stopTicket: (ticketId, token) => {
        axios.defaults.headers.common['Authorization'] =`Bearer ${token}`
        return new Promise((resolve, reject) => {
            axios.put(SERVER_URL + `manager/tickets/${ticketId}/stop`)
                .then((res) => resolve(res.data))
                .catch((err) => reject(err.response.data));
        })
    },

    closeTicket: (ticketId, token) => {
        axios.defaults.headers.common['Authorization'] =`Bearer ${token}`
        return new Promise((resolve, reject) => {
            axios.put(SERVER_URL + `authenticated/tickets/${ticketId}/close`)
                .then((res) => resolve(res.data))
                .catch((err) => reject(err.response.data));
        })
    },

    resolveTicket: (ticketId, token) => {
        axios.defaults.headers.common['Authorization'] =`Bearer ${token}`
        return new Promise((resolve, reject) => {
            axios.put(SERVER_URL + `expert/tickets/${ticketId}/resolve`)
                .then((res) => resolve(res.data))
                .catch((err) => reject(err.response.data));
        })
    },

    reopenTicket: (ticketId, token) => {
        axios.defaults.headers.common['Authorization'] =`Bearer ${token}`
        return new Promise((resolve, reject) => {
            axios.put(SERVER_URL + `client/tickets/${ticketId}/reopen`)
                .then((res) => resolve(res.data))
                .catch((err) => reject(err.response.data));
        })
    },

    // MESSAGES API

    getMessages: (ticketId, token) => {
        axios.defaults.headers.common['Authorization'] =`Bearer ${token}`
        return new Promise((resolve, reject) => {
            axios.get(SERVER_URL + `authenticated/tickets/${ticketId}/chat`)
                .then((res) => resolve(res.data))
                .catch((err) => reject(err.response.data));
        })
    },

    getAttachment: (ticketId, attId, token) => {
        axios.defaults.headers.common['Authorization'] =`Bearer ${token}`
        return new Promise((resolve, reject) => {
            axios.get(SERVER_URL + `authenticated/tickets/${ticketId}/chat/${attId}`)
                .then((res) => resolve(res.data))
                .catch((err) => reject(err.response.data));
        })
    },

    addNewMessage: (ticketId, message, token) => {
        axios.defaults.headers.common['Authorization'] =`Bearer ${token}`

        return new Promise((resolve, reject) => {
            axios.post(SERVER_URL + `authenticated/tickets/${ticketId}/chat`, message ,{
                headers: {
                    "Content-Type": "multipart/form-data"
                }
            })
                .then((res) => resolve(res.data))
                .catch((err) => reject(err.response.data));
        })
    },
}


export default Api
