package com.goodbird.salamanderlib.mclib.utils.resources;

public interface IResourceTransformer
{
    public String transformDomain(String domain, String path);

    public String transformPath(String domain, String path);

    public String transform(String location);
}
