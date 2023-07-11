import { useState, useContext } from 'react';
import { Button, Form, Container, Row, Alert } from 'react-bootstrap';
import { useNavigate } from 'react-router-dom';
import { AuthContext } from "../../utils/AuthContext"
import API from '../../../services/Api';


function ProfileLogin() {
    const authContext = useContext(AuthContext);
    const navigate = useNavigate();

    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');

    const [showAlert, setShowAlert] = useState(false);
    const [showAlert2, setShowAlert2] = useState(false);
    const [alertMsg, setAlertMsg] = useState(false);

    const handleLogout = (event) => {
        event.preventDefault();
        API.logout(authContext.user.token).then((succes) => {
            setShowAlert2(true)
            setAlertMsg("OH YESSSSSS")
        }).catch(err => {
            setShowAlert2(true)
            setAlertMsg("OH NOOOOOOO")
        })
    }

    const handleSubmit = (event) => {
        event.preventDefault();
        const credentials = { "username": email, "password": password };

        API.login(credentials)
            .then((user) => {
                authContext.setUser({
                    id: user.id,
                    email: user.email,
                    token: user.token,
                    username: user.username,
                    name: user.name,
                    surname: user.surname,
                    dateOfBirth: user.dateOfBirth
                });
                console.log(user)
                navigate('/profile');
            })
            .catch((err) => {
                authContext.setUser({
                    id: undefined,
                    email: undefined,
                    token: undefined,
                    username: undefined,
                    name: undefined,
                    surname: undefined,
                    dateOfBirth: undefined
                });
                setShowAlert(true);
            });
    }

    function resetAlert() { setShowAlert('') }
    function handleEmail(ev) { setEmail(ev.target.value) }
    function handlePassword(ev) { setPassword(ev.target.value) }

    return (
        <>
            <Container className='mt-3'>
                {
                    showAlert === true ?
                        <Alert variant="danger" onClose={resetAlert} dismissible>
                            <Alert.Heading>Incorrect username and/or password</Alert.Heading>
                        </Alert> : null
                }
                {
                    showAlert2 === true ?
                        <Alert variant="danger" onClose={resetAlert} dismissible>
                            <Alert.Heading>{alertMsg}</Alert.Heading>
                        </Alert> : null
                }
                <Row>
                    <b style={{ "fontSize": "2rem", "color": 'black', "paddingBottom": "0.3rem" }}>Login</b>
                </Row>
                <Container className="border border-4 rounded" style={{ "marginTop": "0.5rem", "padding": "1rem", "backgroundColor": "white" }}>
                    <Form onSubmit={handleSubmit}>
                        <Form.Group className='mb-2' controlId='email'>
                            <Form.Label>Email:</Form.Label>
                            <Form.Control
                                type="email"
                                value={email}
                                onChange={handleEmail}
                                required={true}
                                placeholder="Enter email" />
                        </Form.Group>
                        <Form.Group className='mb-2' controlId='password'>
                            <Form.Label>Password:</Form.Label>
                            <Form.Control
                                type='password'
                                value={password}
                                onChange={handlePassword}
                                required={true}
                                minLength={6}
                                maxLength={15}
                                placeholder="Password" />
                        </Form.Group>
                        <Form.Group>
                            <Button variant='warning' type='submit' size='lg' onSubmit={handleSubmit}>
                                Login
                            </Button>
                            <Button variant='warning' type='submit' size='lg' onClick={handleLogout}>
                                Logout
                            </Button>
                        </Form.Group>
                    </Form>
                </Container>
            </Container>
        </>
    );

}

export default ProfileLogin;

