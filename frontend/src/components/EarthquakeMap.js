import { MapContainer, TileLayer, CircleMarker, Popup } from 'react-leaflet';

function EarthquakeMap({ earthquakes }) {
    return (
        <MapContainer center={[20, 0]} zoom={2} style={{ height: "500px", width: "100%" }}>

            <TileLayer url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png" />

            {earthquakes.map(eq => (
                <CircleMarker
                    key={eq.id}
                    center={[eq.latitude, eq.longitude]}
                    radius={5}
                    pathOptions={{ color: "red", fillColor: "red", fillOpacity: 0.8 }}
                >
                    <Popup>
                        <b>{eq.title}</b><br />
                        Magnitude: {eq.magnitude}
                    </Popup>
                </CircleMarker>
            ))}

        </MapContainer>
    );
}

export default EarthquakeMap;