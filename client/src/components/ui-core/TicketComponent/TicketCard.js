import Button from "react-bootstrap/Button";
import Card from "react-bootstrap/Card";
import ListGroup from "react-bootstrap/ListGroup";
import { Badge, Row, Col, Form} from "react-bootstrap";
import {useContext, useEffect, useState} from "react";
import useNotification from "../../utils/useNotification";
import {AuthContext} from "../../utils/AuthContext";
import Api from "../../../services/Api";
import {useNavigate} from "react-router-dom";
import {ArrowLeftCircle} from "react-bootstrap-icons";


const priorityMap = {
	1: {
		name: "low",
		status: "success",
	},
	2: {
		name: "medium",
		status: "warning",
	},
	3: {
		name: "high",
		status: "danger",
	},
};

const TicketCard = (props) => {
	const authContext = useContext(AuthContext);
	const notify = useNotification()
	const navigate  = useNavigate();

	const [priority, setPriority] = useState(props.ticket.priority)
	const [expert, setExpert] = useState(props.ticket.expertId);
	const [expertMail, setExpertMail] = useState("");
	const [userMail, setUserMail] = useState("");
	const [experts, setExperts] = useState([]);

	useEffect(() =>{
		if(authContext.user.role == 'manager'){
		Api.getAllExperts(authContext.user.token)
			.then(experts =>{
				setExperts(experts)
				for (var i = 0; i < experts.length; i++) {
					if (experts[i].id == expert) {
						setExpert(expert + " - " + experts[i].email)
						break;
					}
				}
			})
			.catch(err =>{
				notify.error("Server error")
			})}},[])

	useEffect(() => {
		Api.getProfileById(props.ticket.profileId, authContext.user.token).then(
			profile => {
				setUserMail(profile.email)
			}
		).catch(err => {})
		if (expert){
			Api.getProfileById(expert, authContext.user.token)
				.then(expert => {
					setExpertMail(expert.email)
				})
				.catch(err => {
					notify.error("Server error")
				})
		}
		},[])

	const handlePriority = (priority) =>{
		switch (priority.target.value) {
			case 'low':
				setPriority(1)
				break;
			case 'medium':
				setPriority(2)
				break;
			case 'high':
				setPriority(3)
				break;
			default:
				setPriority(null)
		}
	}
	const handleExpert = (expert) =>{
		if (expert.target.value !== 'expert not assigned')
			setExpert(expert.target.value)
		else
			setExpert(null)
	}
	const handleSave = () =>{

				if (!priority || !expert) {
					notify.error('Select both priority and expert')
					return
				}

				Api.modifyPriority(props.ticket.id,priority,authContext.user.token).then(
					notify.success("Ticket modified correctly")
				).catch(
					err =>{
						notify.error('server error')
					}
				)
				const form={
					email: expert? expert.split(' - ')[1].trim():expert,
					priority: priority
				}
				if(expert){
				Api.assignExpert(props.ticket.id,form,authContext.user.token).then(
					notify.success("Ticket modified correctly")
				).catch(
					err =>{
						notify.error('server error')
					}
				)}
				navigate('/dashboard')
	}

	const handleClose = ()=>{
		Api.closeTicket(props.ticket.id,authContext.user.token).then(
			notify.success("Ticket closed successfully")
		).catch(
			err =>{
				notify.error('server error')
			}
		)
		navigate('/dashboard')
	}
	const handleStop = ()=>{
		Api.stopTicket(props.ticket.id,authContext.user.token).then(
			notify.success("Ticket Stopped successfully")
		).catch(
			err =>{

				notify.error('server error')
			}
		)
		navigate('/dashboard')
	}
	const handleReopen = ()=>{
		Api.reopenTicket(props.ticket.id,authContext.user.token).then(
			notify.success("Ticket closed successfully")
		).catch(
			err =>{

				notify.error('server error')
			}
		)
		navigate('/dashboard')
	}
	const handleResolve = ()=>{
		Api.resolveTicket(props.ticket.id,authContext.user.token).then(
			notify.success("Ticket resolve successfully")
		).catch(
			err =>{

				notify.error('server error')
			}
		)
		navigate('/dashboard')
	}

	return (
		<>
			<Row>
				<Col>
		<Card style={{ width: "40rem" }}>
			<Card.Body>
				<Card.Title>
					<Row align="left">
						<Col className='mb-2'>
							<Button variant="danger" onClick={() => navigate('/dashboard')}><ArrowLeftCircle className='me-2'/>Back</Button>
						</Col>
						</Row>

					<Row align="left">
						<Col>
							<b>Ticket {props.ticket.id}</b>
						</Col>

						<Col align="right">
							<Badge bg={"secondary"}>{props.ticket.status}</Badge>
						</Col>
					</Row>
				</Card.Title>
				<ListGroup variant="flush">
					<ListGroup.Item>
						<Row align="left">
							<Col>
								<b>ProductId</b>
							</Col>

							<Col align="right">{props.ticket.productId}</Col>
						</Row>
					</ListGroup.Item>
					<ListGroup.Item>
						<Row align="left">
							<Col>
								<b>category</b>
							</Col>

							<Col align="right">{props.ticket.category}</Col>
						</Row>
					</ListGroup.Item>
					<ListGroup.Item>
						<Row align="left">
							<Col>
								<b>Priority</b>
							</Col>

							<Col align="right">
								{props.ticket.priority == 0 ? <b> - </b>:<Badge bg={priorityMap[props.ticket.priority].status}>{priorityMap[props.ticket.priority].name}</Badge>}
							</Col>
						</Row>
					</ListGroup.Item>
					<ListGroup.Item>
						<Row align="left">
							<Col>
								<b>Created by</b>
							</Col>

							<Col align="right">{userMail}</Col>
						</Row>
					</ListGroup.Item>
					<ListGroup.Item>
						<Row align="left">
							<Col>
								<b>Expert assigned</b>
							</Col>

							<Col align="right">
								{expertMail ? expertMail : 'expert non assigned'}
							</Col>
						</Row>
					</ListGroup.Item>
				<ListGroup>
				</ListGroup>
				</ListGroup>
				<Card.Text className="mt-2">
					<b>Description: </b>
					{props.ticket.description}</Card.Text>
                <ListGroup>
                <Row align="left">
							<Col>
								{authContext.user.role == 'manager' && (props.ticket.status == 'OPEN' || props.ticket.status == 'REOPENED') ?
									<Button className="m-1" variant="success" onClick={handleSave}>Save</Button>:<></>}
							</Col>

							<Col align="right">
								{(props.ticket.status == 'CLOSED' || props.ticket.status == 'RESOLVED') && authContext.user.role == 'user'
									?<Button className="m-1" onClick={handleReopen} variant="success">Reopen</Button>: <></>}
								{props.ticket.status !== 'CLOSED'?

									<Button className="m-1" variant="danger" onClick={handleClose}>Close</Button>:<></>}
								{authContext.user.role == 'manager' && props.ticket.status == 'PROGRESS'?
									<Button className="m-1"  variant="danger" className='ms-1' onClick={handleStop}>Stop</Button>:<></>}
								{authContext.user.role == 'expert' && props.ticket.status == 'PROGRESS'?
									<Button className="m-1" variant="success" className='ms-1' onClick={handleResolve}>Resolve</Button>:<></>}
							</Col>
						</Row>
                </ListGroup>
			</Card.Body>
		</Card>
			</Col>
			<Col className='m-3'>
			<Form.Group className="mb-3">
				<Form.Label><b>CHANGE PRIORITY</b></Form.Label>
				<Form.Select onChange={handlePriority} disabled={authContext.user.role == 'manager' && (props.ticket.status == 'OPEN' || props.ticket.status == 'REOPENED') ? false:true}>
					<option>
						set priority
					</option>
					<option>
						{priorityMap[1].name}
					</option>
					<option>
							{priorityMap[2].name}
					</option>
					<option>
							{priorityMap[3].name}
					</option>
				</Form.Select>

			</Form.Group>
			<Form.Group className="mb-3">
				<Form.Label><b>ASSIGN AN EXPERT</b></Form.Label>
				<Form.Select onChange={handleExpert} disabled={authContext.user.role == 'manager' && (props.ticket.status == 'OPEN' || props.ticket.status == 'REOPENED') ? false:true}>
					{props.ticket.expertId && experts ? experts.filter((e) => e.id == props.ticket.expertId).map((e) =>{
						return(
							<option key={e.id}>{e.id} - { e.email}</option>
						)})
					: <option>expert not assigned</option>
					}
					{experts ? experts.filter((e) => e.id !== props.ticket.expertId).map((e) =>{
						return(
							<option key={e.id}>{e.id} - { e.email}</option>
						)
					}): <> Your not a manager</>}

				</Form.Select>
			</Form.Group>
			</Col>
			</Row>
		</>

	);
};

export default TicketCard;
