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
import ticket from "../../../views/Ticket/Ticket";


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
	const [experts, setExperts] = useState([]);

	useEffect(() =>{
		if(authContext.user.role == 'manager'){
		Api.getAllExperts(authContext.user.token)
			.then(experts =>{
				console.log(experts)
				setExperts(experts)
			})
			.catch(err =>{
				console.log(err)
				notify.error("Server error")
			})}},[])




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
				console.log(`it is not possible to change priority`);
		}
	}
	const handleExpert = (expert) =>{
		console.log(expert.target.value.split(' - ')[1])

		setExpert(expert.target.value)
	}
	const handleSave = () =>{
				Api.modifyPriority(props.ticket.id,priority,authContext.user.token).then(
					notify.success("Ticket modified correctly")
				).catch(
					err =>{
						notify.error('server error')
					}
				)
				const form={
					email: expert.split(' - ')[1].trim(),
					priority: priority
				}
				console.log(form)
				Api.assignExpert(props.ticket.id,form,authContext.user.token).then(
					notify.success("Ticket modified correctly")
				).catch(
					err =>{
						console.log(err)
						notify.error('server error')
					}
				)
				navigate('/dashboard')
	}

	const handleClose = ()=>{
		Api.closeTicket(props.ticket.id,authContext.user.token).then(
			notify.success("Ticket closed successfully")
		).catch(
			err =>{
				console.log(err)
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
				console.log(err)
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
				console.log(err)
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
				console.log(err)
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
								<b>ProfileId</b>
							</Col>

							<Col align="right">{props.ticket.profileId}</Col>
						</Row>
					</ListGroup.Item>
					<ListGroup.Item>
						<Row align="left">
							<Col>
								<b>ExpertId</b>
							</Col>

							<Col align="right">
								{expert ? expert.toString().split(' - ')[0] : 'expert non assigned'}
							</Col>
						</Row>
					</ListGroup.Item>
				<ListGroup>
				</ListGroup>
				</ListGroup>
				<Card.Text>
					<b>Description: </b>
					{props.ticket.description}</Card.Text>
                <ListGroup>
                <Row align="left">
							<Col>
								{authContext.user.role == 'manager' ?<Button variant="success" onClick={handleSave}>Save</Button>:<></>}
							</Col>

							<Col align="right">
								{props.ticket.status == 'CLOSED' && authContext.user.role == 'user'
									?<Button onClick={handleReopen} variant="success">Reopen</Button>: <></>}
								{props.ticket.status !== 'CLOSED'?
									<Button variant="danger" onClick={handleClose}>Close</Button>:<></>}
								{authContext.user.role == 'manager' && props.ticket.status == 'PROGRESS'?
									<Button variant="danger" className='ms-1' onClick={handleStop}>Stop</Button>:<></>}
								{authContext.user.role == 'expert' && props.ticket.status == 'PROGRESS'?
									<Button variant="success" className='ms-1' onClick={handleResolve}>Resolve</Button>:<></>}

							</Col>
						</Row>
                </ListGroup>
			</Card.Body>
		</Card>
			</Col>
			<Col className='m-3'>
			<Form.Group className="mb-3">
				<Form.Label><b>CHANGE PRIORITY</b></Form.Label>
				<Form.Select onChange={handlePriority} disabled={authContext.user.role == 'manager'? false:true}>
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
				<Form.Select onChange={handleExpert} disabled={authContext.user.role == 'manager'? false:true}>
					<option>
						{props.ticket.expertId? props.ticket.expertId : 'expert not assigned'}
					</option>
					{experts ? experts.map((e) =>{
						console.log(e)
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
