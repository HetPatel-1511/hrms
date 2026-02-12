import React, { useEffect } from 'react'
import Button from '../components/Button'
import { useForm } from 'react-hook-form';
import useLoginMutation from '../query/queryHooks/useLoginMutation';
import { saveUser } from '../redux/slices/userSlice';
import { useDispatch } from 'react-redux';
import { useNavigate, useNavigation } from 'react-router';

const Login = () => {
    const {
        register,
        handleSubmit,
        formState: { errors },
    } = useForm();

    const login = useLoginMutation()
    const navigate = useNavigate()
    const dispatch = useDispatch()

    const onSubmit = (data:any) => {
        login.mutate(data);
    };

    useEffect(()=>{
        if (login.isSuccess) {
            dispatch(saveUser(login.data.data))
            navigate("/travel-plan")
        }
    },[login.data])

    return (
        <div className='min-h-screen'>
            <h1 className='text-4xl mt-10 font-bold'>Login</h1>
            <form className="max-w-sm mx-auto py-32">
                <div className="mb-5">
                    <label
                        htmlFor="email"
                        className="block mb-2.5 text-sm font-medium text-heading"
                    >
                        Your email
                    </label>
                    <input
                        type="email"
                        id="email"
                        {...register("email", { required: "Email is mandatory", pattern:{value:/^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/, message: "Invalid email address"} })}
                        className="bg-neutral-secondary-medium border border-default-medium text-heading text-sm rounded-base focus:ring-brand focus:border-brand block w-full px-3 py-2.5 shadow-xs placeholder:text-body"
                        placeholder="name@flowbite.com"
                        required
                    />
                    {errors.email && <span style={{ color: "red" }}>{errors.email.message?.toString()}</span>}
                </div>
                <div className="mb-5">
                    <label
                        htmlFor="password"
                        className="block mb-2.5 text-sm font-medium text-heading"
                    >
                        Your password
                    </label>
                    <input
                        type="password"
                        id="password"
                        {...register("password", { required: "Password is mandatory", minLength:{value:8, message: "Minimum length of password is 8"} })}
                        className="bg-neutral-secondary-medium border border-default-medium text-heading text-sm rounded-base focus:ring-brand focus:border-brand block w-full px-3 py-2.5 shadow-xs placeholder:text-body"
                        placeholder="••••••••"
                        required
                    />
                {errors.password && <span style={{ color: "red" }}>{errors.password.message?.toString()}</span>}
                </div>
                <Button
                    onClick={handleSubmit(onSubmit)}
                >
                    Submit
                </Button>
            </form>
        </div>
    )
}

export default Login
