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

export default [
    {
        path: "/",
        element: <AuthLayout />,
        children: [
            {
                path: "travel-plan",
                children: [
                    {
                        index: true,
                        element: <TravelPlan />,
                    },
                    {
                        path: "add",
                        element: <AddTravelPlan />,
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
                        path: ":travelPlanId/employee/:employeeId/expenses/add",
                        element: <AddTravelPlanEmployeeExpenses />,
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
    }
]