import { useState } from "react";
import { useNavigate } from 'react-router-dom';
import {Form, Row, Col, Button, Container} from "react-bootstrap";
import Api from "../../../services/Api";
import dayjs from 'dayjs';
import { useLocation } from 'react-router-dom'


function ProfileForm(props) {

    const location = useLocation();
    const isAdding = !location.state;

    const [email, setEmail] = useState(location.state ? location.state.email :"");
    const [name, setName] = useState(location.state ? location.state.name:"");
    const [surname, setSurname] = useState(location.state ? location.state.surname:"");
    const [username, setUsername] = useState(location.state ? location.state.username:"");
    const [password, setPassword] = useState("");
    const [date, setDate] = useState(location.state ? location.state.dateOfBirth:undefined);

    const navigate = useNavigate();

    const handleSubmit = async(event) => {
        event.preventDefault();

        const profile =
            { email: email, name: name, surname: surname, username: username, dateOfBirth: dayjs(date).format("YYYY-MM-DD") , hash: password};
        if (isAdding) {
            // API CALL
            Api.addNewProfile(profile)
                .then(profile => {
                    console.log(profile)
                })
                .catch(err => {
                    console.log('errore' + err.toString());
                })
        } else {
            Api.modifyProfile(profile.email, profile)
                .then(profile => {
                    console.log(profile)
                })
                .catch(err => {
                    console.log('errore' + err.toString());
                })
        }
        navigate(-1);
    }

    return (
            <Container className='profile-form' >
                <Form className="profile-form" onSubmit={handleSubmit}>

                    <Form.Group className="mb-3">
                        <Form.Label>Email</Form.Label>
                        <Form.Control type="email" maxLength={50} required={true} disabled={!isAdding} value={email} onChange={event => setEmail(event.target.value)}/>
                    </Form.Group>

                    <Form.Group className="mb-3">
                        <Form.Label>Username</Form.Label>
                        <Form.Control type="text" maxLength={50} required={true} value={username} onChange={event => setUsername(event.target.value)}/>
                    </Form.Group>

                    <Form.Group className="mb-3">
                        <Form.Label>Name</Form.Label>
                        <Form.Control type="text" maxLength={50} required={true} value={name} onChange={event => setName(event.target.value)}/>
                    </Form.Group>

                    <Form.Group className="mb-3">
                        <Form.Label>Date of Birth</Form.Label>
                        <Form.Control type="date" required={true} max={dayjs().format('YYYY-MM-DD')} value={date} onChange={event => setDate(event.target.value)}/>
                    </Form.Group>

                    <Form.Group className="mb-3">
                        <Form.Label>Surname</Form.Label>
                        <Form.Control type="text" maxLength={50} required={true} value={surname} onChange={event => setSurname(event.target.value)}/>
                    </Form.Group>

                    <Form.Group className="mb-3">
                        <Form.Label>Password</Form.Label>
                        <Form.Control type="password" maxLength={16} required={true} value={password} onChange={event => setPassword(event.target.value)}/>
                    </Form.Group>

                    <Button variant="primary" type="submit">{isAdding ? "Create" : "Edit"}</Button>
                </Form>
            </Container>
    );

}

export { ProfileForm }
