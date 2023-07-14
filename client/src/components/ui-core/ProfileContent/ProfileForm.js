import {useContext, useState} from "react";
import { useNavigate } from 'react-router-dom';
import {Form, Button, Container} from "react-bootstrap";
import Api from "../../../services/Api";
import dayjs from 'dayjs';
import { useLocation } from 'react-router-dom'
import useNotification from "../../utils/useNotification";
import {AuthContext} from "../../utils/AuthContext";

function ProfileForm() {
    const authContext = useContext(AuthContext);

    const location = useLocation();
    const isAdding = !location.state; //Checks whether the profile variable exists (set in ProfileContent when clicking on edit)

    const [email, setEmail] = useState(location.state ? location.state.email :"");
    const [name, setName] = useState(location.state ? location.state.name:"");
    const [surname, setSurname] = useState(location.state ? location.state.surname:"");
    const [username, setUsername] = useState(location.state ? location.state.username:"");
    const [password, setPassword] = useState("");
    const [date, setDate] = useState(location.state ? location.state.dateOfBirth:dayjs().format('YYYY-MM-DD'));

    const navigate = useNavigate();
    const notify = useNotification()

    const handleSubmit = async(event) => {

        event.preventDefault();

        const profile =
            { email: email, name: name, surname: surname, username: username, dateOfBirth: dayjs(date).format("YYYY-MM-DD") , password: password};
        if (isAdding) {
            // API CALL
            Api.signup(profile)
                .then(() => {
                    notify.success("New profile created!")
                    navigate("/login");
                })
                .catch(err => {
                    notify.error(err.title ? err.title.toString() : "Server error")
                })
        } else {
            Api.modifyProfile(profile.email, profile, authContext.user.token)
                .then(() => {
                    notify.success("Profile modified successfully")
                    navigate("/profile");
                })
                .catch(err => {
                    notify.error(err.title ? err.title.toString() : "Server error")
                })
        }
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
                        <Form.Label>Surname</Form.Label>
                        <Form.Control type="text" maxLength={50} required={true} value={surname} onChange={event => setSurname(event.target.value)}/>
                    </Form.Group>

                    <Form.Group className="mb-3">
                        <Form.Label>Date of Birth</Form.Label>
                        <Form.Control type="date" required={true} max={dayjs().format('YYYY-MM-DD')} value={date} onChange={event => setDate(dayjs(event.target.value).format('YYYY-MM-DD'))}/>
                    </Form.Group>
                    <Form.Group className="mb-3">
                        <Form.Label>Password</Form.Label>
                        <Form.Control type="password" maxLength={16} required={true} value={password} onChange={event => setPassword(event.target.value)}/>
                    </Form.Group>
                    <Button className="mt-2" variant="primary" type="submit">{isAdding ? "Create" : "Submit"}</Button>
                </Form>
            </Container>
    );

}

export { ProfileForm }
