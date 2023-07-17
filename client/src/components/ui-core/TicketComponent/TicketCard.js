import Button from "react-bootstrap/Button";
import Card from "react-bootstrap/Card";
import ListGroup from "react-bootstrap/ListGroup";
import { Badge, Row, Col, Form} from "react-bootstrap";
import Dropdown from "react-bootstrap/Dropdown";
import DropdownButton from "react-bootstrap/DropdownButton";
import {useState} from "react";


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

	const [priority, setPriority] = useState(props.ticket.priority)
	const [expert, setExpert] = useState(props.ticket.expertId);

	const handlePriority = (priority) =>{
		console.log(priority.target.value)
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
		console.log(expert.target.value)

		setExpert(expert.target.value)
	}

	return (
		<>
			<Row>
				<Col>
		<Card style={{ width: "40rem" }}>
			<Card.Body>
				<Card.Title>
					<Row align="left">
						<Col>
							<b>Ticket {props.ticket.id}</b>
						</Col>

						<Col align="right">
							<Badge bg={"success"}>Available</Badge>
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
								<Badge bg={priorityMap[priority].status}>
									{priorityMap[priority].name}
								</Badge>
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
								{expert ? expert : 'expert non assigned'}
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
								<Button variant="success">Save</Button>
							</Col>

							<Col align="right"><Button variant="danger">Close</Button></Col>
						</Row>
                </ListGroup>
			</Card.Body>
		</Card>
			</Col>
			<Col className='m-3'>
			<Form.Group className="mb-3">
				<Form.Label><b>CHANGE PRIORITY</b></Form.Label>
				<Form.Select onChange={handlePriority}>
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
				<Form.Select onChange={handleExpert}>
					<option>Disabled select</option>
					<option> 1</option>
					<option> 2</option>
					<option> 3</option>
					<option> 4</option>

				</Form.Select>
			</Form.Group>
			</Col>
			</Row>
		</>

	);
};

export default TicketCard;
