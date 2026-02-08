import { Outlet } from "react-router";

export default [
    {
        path: "/",
        element: <><h1>parent<Outlet /></h1></>,
        children: [
        {
            path: "child",
            element: <><h1>child</h1></>,
        },
        ],
    }
]