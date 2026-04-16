import React, { useState } from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import 'leaflet/dist/leaflet.css';
import EarthquakeTable from './components/EarthquakeTable';
import EarthquakeMap from './components/EarthquakeMap';
import api from './services/api';

function App() {
    //states
    const [earthquakes, setEarthquakes] = useState([]);
    const [loading, setLoading] = useState(false);
    const [message, setMessage] = useState('');
    const [filterDate, setFilterDate] = useState('');

    //fetches new data from the API and saves it to the database
    const handleFetch = async () => {
        setLoading(true);
        setMessage('');
        try {
            const response = await api.fetchAndStore();
            setEarthquakes(response.data);
            setMessage(`Fetched ${response.data.length} earthquakes.`);
        } catch (error) {
            setMessage('Failed to fetch earthquakes.');
        } finally {
            setLoading(false);
        }
    };

    // read all the earthquakes from db
    const handleGetAll = async () => {
        setLoading(true);
        setMessage('');
        try {
            const response = await api.getAll();
            setEarthquakes(response.data);
            setMessage(`Loaded ${response.data.length} earthquakes.`);
        } catch (error) {
            setMessage('Failed to load earthquakes.');
        } finally {
            setLoading(false);
        }
    };

    // filter by time
    const handleFilterByTime = async () => {
        if (!filterDate) {
            setMessage('Please select a date first.');
            return;
        }
        setLoading(true);
        setMessage('');
        try {
            const timestamp = new Date(filterDate).getTime();
            const response = await api.getAfter(timestamp);
            setEarthquakes(response.data);
            setMessage(`Found ${response.data.length} earthquakes after selected date.`);
        } catch (error) {
            setMessage('Failed to filter earthquakes.');
        } finally {
            setLoading(false);
        }
    };

    // delete 
    const handleDelete = async (id) => {
        try {
            await api.deleteEarthquake(id);
            setEarthquakes(earthquakes.filter(eq => eq.id !== id));
            setMessage('Earthquake deleted successfully.');
        } catch (error) {
            setMessage('Failed to delete earthquake.');
        }
    };

    return (
<div className="container mt-4">
            <h1 className="mb-4">Earthquake</h1>

            {/* buttons */}
            <div className="mb-3">
                <button
                    className="btn btn-outline-dark me-2"
                    onClick={handleFetch}
                    disabled={loading}
                >
                    {loading ? 'Loading...' : 'Fetch New'}
                </button>

                <button
                    className="btn btn-outline-secondary"
                    onClick={handleGetAll}
                    disabled={loading}
                >
                    Show All
                </button>
            </div>

            {/* filter by time */}
            <div className="mb-3 d-flex align-items-center gap-2">
                <input
                    type="datetime-local"
                    className="form-control w-auto"
                    value={filterDate}
                    onChange={(e) => setFilterDate(e.target.value)}
                />
                <button
                    className="btn btn-outline-secondary"
                    onClick={handleFilterByTime}
                    disabled={loading}
                >
                    Filter
                </button>
            </div>

            {/* message */}
            {message && (
                <div className="alert alert-secondary">{message}</div>
            )}

            {/* table */}
            <EarthquakeTable
                earthquakes={earthquakes}
                onDelete={handleDelete}
            />

            {/* map */}
            <EarthquakeMap earthquakes={earthquakes} />
        </div>
    );
}

export default App;