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

    //Authentication API

    login: (credentials) => {
        //axios.defaults.headers.post['Content-Type'] ='application/x-www-form-urlencoded';
        return new Promise((resolve, reject) => {
            axios.post(SERVER_URL + 'public/login', credentials)
                .then((res) => resolve(res.data))
                .catch((err) => reject(err.response.data));
        })
    },

    logout: (access_token) => {
        return new Promise((resolve, reject) => {
            axios.delete(SERVER_URL + 'authenticated/logout', {
                headers: {
                    'Authorization': `${access_token}`
                }
            }).then((res) => resolve(res.data))
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

    getProfileByEmail: (email) => {
        return new Promise((resolve, reject) => {
            axios.get(SERVER_URL + `/authenticated/profiles/${email}`)
                .then((res) => resolve(res.data))
                .catch((err) => reject(err.response.data));
        })
    },
    addNewProfile: (formData) => {
        return new Promise((resolve, reject) => {
            axios.post(SERVER_URL + 'authenticated/profiles', formData)
                .then((res) => resolve(res.data))
                .catch((err) => reject(err.response.data));
        })
    },
    modifyProfile: (email, formData) => {
        return new Promise((resolve, reject) => {
            axios.put(SERVER_URL + `authenticated/profiles/${email}`, formData)
                .then((res) => resolve(res.data))
                .catch((err) => reject(err.response.data));
        })
    },

    // TICKET API

    getTicketById: (ticketId) => {
        return new Promise((resolve, reject) => {
            axios.get(SERVER_URL + `authenticated/tickets/${ticketId}`)
                .then((res) => resolve(res.data))
                .catch((err) => reject(err.response.data));
        })
    },

    getOpenTickets: () => {
        return new Promise((resolve, reject) => {
            axios.get(SERVER_URL + `manager/tickets/open`)
                .then((res) => resolve(res.data))
                .catch((err) => reject(err.response.data));
        })
    },

    getTicketsByProfile: (email) => {
        return new Promise((resolve, reject) => {
            axios.get(SERVER_URL + `client/tickets/created/${email}`)
                .then((res) => resolve(res.data))
                .catch((err) => reject(err.response.data));
        })
    },

    getAssignedTickets: (email) => {
        return new Promise((resolve, reject) => {
            axios.get(SERVER_URL + `expert/tickets/assigned/${email}`)
                .then((res) => resolve(res.data))
                .catch((err) => reject(err.response.data));
        })
    },

    addNewTicket: (ticket) => {
        return new Promise((resolve, reject) => {
            axios.post(SERVER_URL + 'client/tickets', ticket)
                .then((res) => resolve(res.data))
                .catch((err) => reject(err.response.data));
        })
    },

    modifyPriority: (ticketId, priority) => {
        return new Promise((resolve, reject) => {
            axios.put(SERVER_URL + `manager/tickets/${ticketId}/priority/${priority}`)
                .then((res) => resolve(res.data))
                .catch((err) => reject(err.response.data));
        })
    },

    assignExpert: (ticketId, form) => {
        return new Promise((resolve, reject) => {
            axios.put(SERVER_URL + `manager/tickets/${ticketId}/expert`, form)
                .then((res) => resolve(res.data))
                .catch((err) => reject(err.response.data));
        })
    },

    stopTicket: (ticketId) => {
        return new Promise((resolve, reject) => {
            axios.put(SERVER_URL + `manager/tickets/${ticketId}/stop`)
                .then((res) => resolve(res.data))
                .catch((err) => reject(err.response.data));
        })
    },

    closeTicket: (ticketId) => {
        return new Promise((resolve, reject) => {
            axios.put(SERVER_URL + `expert/tickets/${ticketId}/close`)
                .then((res) => resolve(res.data))
                .catch((err) => reject(err.response.data));
        })
    },

    resolveTicket: (ticketId) => {
        return new Promise((resolve, reject) => {
            axios.put(SERVER_URL + `expert/tickets/${ticketId}/resolve`)
                .then((res) => resolve(res.data))
                .catch((err) => reject(err.response.data));
        })
    },

    reopenTicket: (ticketId) => {
        return new Promise((resolve, reject) => {
            axios.put(SERVER_URL + `client/tickets/${ticketId}/reopen`)
                .then((res) => resolve(res.data))
                .catch((err) => reject(err.response.data));
        })
    },

    // MESSAGES API

    getMessages: (ticketId) => {
        return new Promise((resolve, reject) => {
            axios.get(SERVER_URL + `authenticated/tickets/${ticketId}/chat`)
                .then((res) => resolve(res.data))
                .catch((err) => reject(err.response.data));
        })
    },

    getAttachment: (ticketId, attId) => {
        return new Promise((resolve, reject) => {
            axios.get(SERVER_URL + `authenticated/tickets/${ticketId}/chat/${attId}`)
                .then((res) => resolve(res.data))
                .catch((err) => reject(err.response.data));
        })
    },

    addNewMessage: (ticketId, message) => {
        return new Promise((resolve, reject) => {
            axios.post(SERVER_URL + `authenticated/tickets/${ticketId}/chat`, message)
                .then((res) => resolve(res.data))
                .catch((err) => reject(err.response.data));
        })
    },
}


export default Api
