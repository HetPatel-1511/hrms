import React, { useEffect } from 'react'
import useConfigurationQuery from '../query/queryHooks/useConfigurationQuery'
import Loading from '../components/Loading'
import ServerError from '../components/ServerError'
import FormInput from '../components/TextInput'
import { useForm } from 'react-hook-form'
import Button from '../components/Button'
import useAddConfigurationMutation from '../query/queryHooks/useAddConfigurationMutation'
import { useNavigate } from 'react-router'

const AddConfiguration = () => {
    const {
        register,
        handleSubmit,
        formState: { errors },
        setValue
    } = useForm();

    const navigate = useNavigate();

    const addConfiguration = useAddConfigurationMutation()

    const onSubmit = (data: any) => {
        addConfiguration.mutate(data);
    };

    useEffect(() => {
        if (addConfiguration.isSuccess) {
            navigate(`/configuration`)
        }
    }, [addConfiguration.isSuccess])

    return (
        <div>
            <form className="">
                <div className="mb-5">
                    <FormInput
                        label="Key"
                        id="configKey"
                        placeholder="manager_email"
                        register={register}
                        errors={errors}
                        validation={{ required: 'Key is mandatory' }}
                    />
                    <FormInput
                        label="Value"
                        id="configValue"
                        placeholder="manager@example.com"
                        register={register}
                        errors={errors}
                        validation={{ required: 'Value is mandatory' }}
                    />
                    <Button onClick={handleSubmit(onSubmit)}>
                        Submit
                    </Button>
                </div>
            </form>
        </div>
    )
}

export default AddConfiguration
