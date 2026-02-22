import { Outlet } from "react-router";
import UnAuthLayout from "../components/UnAuthLayout";
import AuthLayout from "../components/AuthLayout";
import Login from "../pages/Login";
import TravelPlan from "../pages/TravelPlan";
import AddTravelPlan from "../pages/AddTravelPlan";
import SingleTravelPlan from "../pages/SingleTravelPlan";
import TravelPlanEmployeeDocuments from "../pages/TravelPlanEmployeeDocuments";
import TravelPlanEmployeeExpenses from "../pages/TravelPlanEmployeeExpenses";
import AddTravelPlanEmployeeExpenses from "../pages/AddTravelPlanEmployeeExpenses";
import Employee from "../pages/Employee";
import SingleEmployee from "../pages/SingleEmployee";
import UserProfile from "../pages/UserProfile";
import Configuration from "../pages/Configuration";
import AddConfiguration from "../pages/AddConfiguration";
import EditConfiguration from "../pages/EditConfiguration";
import JobOpenings from "../pages/JobOpenings";
import AddJobOpening from "../pages/AddJobOpening";
import SingleJobOpening from "../pages/SingleJobOpening";
import ShareJob from "../pages/ShareJob";
import ReferFriend from "../pages/ReferFriend";
import Games from "../pages/Games";
import AddGames from "../pages/AddGames";
import EditGames from "../pages/EditGames";
import SingleGame from "../pages/SingleGame";
import GameBookings from "../pages/GameBookings";
import NotFound404 from "../pages/NotFound404";
import Authorize from "../components/Authorize";

export default [
    {
        path: "/",
        element: <AuthLayout />,
        children: [
            {
                path: "travel-plan",
                children: [
                    {
                        path: "",
                        element: <Authorize roles={["HR"]} />,
                        children: [
                            {
                                index: true,
                                element: <TravelPlan />,
                            },
                        ]
                    },
                    {
                        path: "me",
                        element: <TravelPlan isMy={true} />,
                    },
                    {
                        path: "add",
                        element: <Authorize roles={["HR"]} />,
                        children: [
                            {
                                index: true,
                                element: <AddTravelPlan />
                            },
                        ]
                    },
                    {
                        path: ":travelPlanId",
                        element: <SingleTravelPlan />,
                    },
                    {
                        path: ":travelPlanId/employee/:employeeId/documents",
                        element: <TravelPlanEmployeeDocuments />,
                    },
                    {
                        path: ":travelPlanId/employee/:employeeId/expenses",
                        element: <TravelPlanEmployeeExpenses />,
                    },
                    {
                        path: ":travelPlanId/employee/:employeeId/expenses/:expenseId/draft",
                        element: <AddTravelPlanEmployeeExpenses isDraftScreen={true} />,
                    },
                    {
                        path: ":travelPlanId/employee/:employeeId/expenses/add",
                        element: <AddTravelPlanEmployeeExpenses />,
                    },
                ]
            },
            {
                path: "employee",
                children: [
                    {
                        index: true,
                        element: <Employee />
                    },
                    {
                        path: ":employeeId",
                        element: <SingleEmployee />
                    },
                    {
                        path: ":id/profile",
                        element: <UserProfile />
                    }
                ]
            },
            {
                path: "job-openings",
                children: [
                    {
                        index: true,
                        element: <JobOpenings />
                    },
                    {
                        path: "add",
                        element: <Authorize roles={["HR"]} />,
                        children: [
                            {
                                index: true,
                                element: <AddJobOpening />
                            },
                        ]
                    },
                    {
                        path: ":jobId",
                        element: <SingleJobOpening />,
                        children: [
                            {
                                path: "share",
                                element: <ShareJob />
                            },
                            {
                                path: "refer",
                                element: <ReferFriend />
                            },
                        ]
                    },
                ]
            },
            {
                path: "games",
                children: [
                    {
                        index: true,
                        element: <Games />
                    },
                    {
                        path: ":gameId",
                        element: <SingleGame />
                    },
                    {
                        path: "add",
                        element: <Authorize roles={["HR"]} />,
                        children: [
                            {
                                index: true,
                                element: <AddGames />
                            },
                        ]
                    },
                    {
                        path: ":gameId/edit",
                        element: <Authorize roles={["HR"]} />,
                        children: [
                            {
                                index: true,
                                element: <EditGames />
                            },
                        ]
                    },
                    {
                        path: "employee/:employeeId/bookings",
                        element: <GameBookings />
                    }
                ]
            },
            {
                path: "configuration",
                element: <Authorize roles={["HR"]} />,
                children: [
                    {
                        index: true,
                        element: <Configuration />
                    },
                    {
                        path: "add",
                        element: <AddConfiguration />
                    },
                    {
                        path: ":configKey/edit",
                        element: <EditConfiguration />
                    },
                ]
            },
        ],
    },
    {
        path: "/auth",
        element: <UnAuthLayout />,
        children: [
            {
                path: "login",
                element: <Login />
            }
        ]
    },
    {
        path: "*",
        element: <NotFound404 />
    }
]