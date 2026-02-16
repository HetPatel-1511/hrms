import React, { useEffect } from 'react'
import useConfigurationQuery from '../query/queryHooks/useConfigurationQuery'
import Loading from '../components/Loading'
import ServerError from '../components/ServerError'
import FormInput from '../components/TextInput'
import { useForm } from 'react-hook-form'
import Button from '../components/Button'
import useAddConfigurationMutation from '../query/queryHooks/useAddConfigurationMutation'
import { useNavigate, useParams } from 'react-router'
import useEditConfigurationMutation from '../query/queryHooks/useEditConfigurationMutation'
import useSingleConfigurationQuery from '../query/queryHooks/useSingleConfigurationQuery'

const EditConfiguration = () => {
    const { configKey } = useParams();

    const {
        register,
        handleSubmit,
        formState: { errors },
        setValue
    } = useForm();

    const navigate = useNavigate();

    const editConfiguration = useEditConfigurationMutation()
    const { data } = useSingleConfigurationQuery(configKey);

    const onSubmit = (data: any) => {
        editConfiguration.mutate(data);
    };

    useEffect(() => {
        if (editConfiguration.isSuccess) {
            navigate(`/configuration`)
        }
    }, [editConfiguration.isSuccess])

    useEffect(() => {
        if (data?.data?.configKey) {
            setValue("configKey", data?.data?.configKey);
            setValue("configValue", data?.data.configValue);
        }
    }, [data])

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
                        disabled={true}
                        readOnly
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

export default EditConfiguration
