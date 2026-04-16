import React from 'react';

// table that shows the earthquakes
function EarthquakeTable({ earthquakes, onDelete }) {

    // tf the list is empty, show msg
    if (earthquakes.length === 0) {
        return <p>No earthquakes found.</p>;
    }

    return (
        <table className="table table-bordered">
            <thead>
                <tr>
                    <th>Title</th>
                    <th>Magnitude</th>
                    <th>Mag Type</th>
                    <th>Place</th>
                    <th>Time</th>
                    <th>Delete</th>
                </tr>
            </thead>
            <tbody>
                {/* loops through every earthquake and renders it in a row*/}
                {earthquakes.map((eq) => (
                    <tr key={eq.id}>
                        <td>{eq.title}</td>
                        <td>{eq.magnitude}</td>
                        <td>{eq.magType}</td>
                        <td>{eq.place}</td>
                        {/* time is Unix timestamp, we convert to clasic date */}
                        <td>{new Date(eq.time).toLocaleString()}</td>
                        <td>
                            <button
                                className="btn btn-danger btn-sm"
                                onClick={() => onDelete(eq.id)}
                            >
                                Delete
                            </button>
                        </td>
                    </tr>
                ))}
            </tbody>
        </table>
    );
}

export default EarthquakeTable;