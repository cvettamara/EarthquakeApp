import axios from 'axios';

const BASE_URL = 'http://localhost:8080/api/earthquakes';

//all calls to backend API
const api = {

    // fetch and store earthquakes
    fetchAndStore: () => {
        return axios.post(`${BASE_URL}/fetch`);
    },

    // returns from db
    getAll: () => {
        return axios.get(BASE_URL);
    },

    // returns wuth given timestamp
    getAfter: (timestamp) => {
        return axios.get(`${BASE_URL}/after/${timestamp}`);
    },

    // delete
    deleteEarthquake: (id) => {
        return axios.delete(`${BASE_URL}/${id}`);
    }
};

export default api;